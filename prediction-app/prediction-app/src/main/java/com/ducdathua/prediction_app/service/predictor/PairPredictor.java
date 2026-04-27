package com.ducdathua.prediction_app.service.predictor;

import com.ducdathua.prediction_app.model.Result;
import com.ducdathua.prediction_app.service.PairGlobalStoreService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class PairPredictor implements Predictor<Map<Integer, List<PairPredictor.TargetTraceDto>>> {

    private static final double W_7D = 0.4;
    private static final double W_30D = 0.3;
    private static final double W_90D = 0.2;
    private static final double W_180D = 0.1;

    private final PairGlobalStoreService globalStoreService;

    public PairPredictor(PairGlobalStoreService globalStoreService) {
        this.globalStoreService = globalStoreService;
    }
    @Override
    public String getName() {
        return "PAIR_TO_NEXT";
    }

    @Override
    public Map<Integer, List<TargetTraceDto>> predict(List<Result> allResults) {

        if (allResults == null || allResults.size() < 2) {
            return Map.of(-1, List.of());
        }

        allResults.sort(Comparator.comparing(Result::getDate));

        Map<Integer, Map<Integer, Double>> globalMemory = buildGlobalMemory(allResults);
        globalStoreService.saveGlobal(globalMemory);

        Result today = allResults.get(allResults.size() - 1);

        return applyTodayContext(globalMemory, today);
    }

    // =========================================================
    // 1. GLOBAL MEMORY
    // pairKey -> targetNumber -> score
    // =========================================================

    public Map<Integer, Map<Integer, Double>> buildGlobalMemory(List<Result> allResults) {

        Map<Integer, Map<Integer, Double>> memory = new HashMap<>();

        if (allResults == null || allResults.size() < 2) {
            return memory;
        }

        allResults.sort(Comparator.comparing(Result::getDate));

        LocalDate latestDate = allResults.get(allResults.size() - 1).getDate();

        for (int i = 0; i < allResults.size() - 1; i++) {

            Result current = allResults.get(i);
            Result next = allResults.get(i + 1);

            long daysDiff = ChronoUnit.DAYS.between(current.getDate(), latestDate);
            double weight = getWeight(daysDiff);

            if (weight <= 0) {
                continue;
            }

            Set<Integer> currentSet = toSet(current);
            Set<Integer> nextSet = toSet(next);

            List<Integer> currentNumbers = new ArrayList<>(currentSet);

            for (int a = 0; a < currentNumbers.size(); a++) {
                for (int b = a + 1; b < currentNumbers.size(); b++) {

                    int first = currentNumbers.get(a);
                    int second = currentNumbers.get(b);

                    int pairKey = encodePair(first, second);

                    memory.putIfAbsent(pairKey, new HashMap<>());

                    Map<Integer, Double> targetScoreMap = memory.get(pairKey);

                    for (Integer target : nextSet) {
                        targetScoreMap.put(
                                target,
                                targetScoreMap.getOrDefault(target, 0.0) + weight
                        );
                    }
                }
            }
        }

        return memory;
    }

    // =========================================================
    // 2. TODAY CONTEXT
    // Chỉ dùng các cặp xuất hiện trong ngày mới nhất
    // targetNumber -> sources(pair -> score)
    // =========================================================

    public Map<Integer, List<TargetTraceDto>> applyTodayContext(
            Map<Integer, Map<Integer, Double>> globalMemory,
            Result today
    ) {

        if (globalMemory == null || globalMemory.isEmpty() || today == null) {
            return Map.of(-1, List.of());
        }

        Map<Integer, Map<Integer, Double>> traceMap = new HashMap<>();
        // targetNumber -> pairKey -> score

        Set<Integer> todaySet = toSet(today);
        List<Integer> todayNumbers = new ArrayList<>(todaySet);

        for (int a = 0; a < todayNumbers.size(); a++) {
            for (int b = a + 1; b < todayNumbers.size(); b++) {

                int first = todayNumbers.get(a);
                int second = todayNumbers.get(b);

                int pairKey = encodePair(first, second);

                Map<Integer, Double> targetScoreMap = globalMemory.get(pairKey);

                if (targetScoreMap == null || targetScoreMap.isEmpty()) {
                    continue;
                }

                for (Map.Entry<Integer, Double> entry : targetScoreMap.entrySet()) {

                    int targetNumber = entry.getKey();
                    double score = entry.getValue();

                    traceMap.putIfAbsent(targetNumber, new HashMap<>());

                    Map<Integer, Double> sourceMap = traceMap.get(targetNumber);

                    sourceMap.put(
                            pairKey,
                            sourceMap.getOrDefault(pairKey, 0.0) + score
                    );
                }
            }
        }

        List<TargetTraceDto> result = traceMap.entrySet()
                .stream()
                .map(entry -> {

                    int targetNumber = entry.getKey();
                    Map<Integer, Double> sourceMap = entry.getValue();

                    double totalScore = sourceMap.values()
                            .stream()
                            .mapToDouble(Double::doubleValue)
                            .sum();

                    List<PairSourceDto> sources = sourceMap.entrySet()
                            .stream()
                            .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                            .limit(5) // 🔥 THÊM DÒNG NÀY
                            .map(e -> new PairSourceDto(
                                    e.getKey(),
                                    decodePair(e.getKey()),
                                    e.getValue()
                            ))
                            .toList();

                    return new TargetTraceDto(
                            targetNumber,
                            totalScore,
                            sources
                    );
                })
                .sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
                .toList();

        return Map.of(-1, result);
    }

    // =========================================================
    // HELPERS
    // =========================================================

    private Set<Integer> toSet(Result result) {
        Set<Integer> set = new HashSet<>();

        if (result.getNumbers() != null) {
            set.addAll(result.getNumbers());
        }

        set.add(result.getSingleNumber());

        return set;
    }

    private int encodePair(int a, int b) {
        int min = Math.min(a, b);
        int max = Math.max(a, b);

        return min * 100 + max;
    }

    private String decodePair(int pairKey) {
        int a = pairKey / 100;
        int b = pairKey % 100;

        return String.format("%02d-%02d", a, b);
    }

    private double getWeight(long daysDiff) {
        if (daysDiff <= 7) return W_7D;
        if (daysDiff <= 30) return W_30D;
        if (daysDiff <= 90) return W_90D;
        if (daysDiff <= 180) return W_180D;

        return 0;
    }

    // =========================================================
    // DTO NESTED
    // =========================================================

    public static class TargetTraceDto {

        private int number;
        private double score;
        private List<PairSourceDto> sources;

        public TargetTraceDto() {
        }

        public TargetTraceDto(int number, double score, List<PairSourceDto> sources) {
            this.number = number;
            this.score = score;
            this.sources = sources;
        }

        public int getNumber() {
            return number;
        }

        public double getScore() {
            return score;
        }

        public List<PairSourceDto> getSources() {
            return sources;
        }
    }

    public static class PairSourceDto {

        private int pairKey;
        private String pair;
        private double score;

        public PairSourceDto() {
        }

        public PairSourceDto(int pairKey, String pair, double score) {
            this.pairKey = pairKey;
            this.pair = pair;
            this.score = score;
        }

        public int getPairKey() {
            return pairKey;
        }

        public String getPair() {
            return pair;
        }

        public double getScore() {
            return score;
        }
    }
}