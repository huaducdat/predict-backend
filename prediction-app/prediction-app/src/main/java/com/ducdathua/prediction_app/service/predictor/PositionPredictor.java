package com.ducdathua.prediction_app.service.predictor;

import com.ducdathua.prediction_app.dto.NumberScoreDto;
import com.ducdathua.prediction_app.model.Result;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class PositionPredictor implements Predictor<Map<Integer, List<NumberScoreDto>>> {

    private static final int GROUP_SIZE = 10;

    private static final double W_30D = 0.5;
    private static final double W_90D = 0.3;
    private static final double W_180D = 0.2;

    @Override
    public String getName() {
        return "POSITION";
    }

    @Override
    public Map<Integer, List<NumberScoreDto>> predict(List<Result> allResults) {

        if (allResults == null || allResults.size() < 2) {
            return Map.of();
        }

        allResults.sort(Comparator.comparing(Result::getDate));
        LocalDate latestDate = allResults.get(allResults.size() - 1).getDate();

        // 🔥 1. GLOBAL (logic cũ giữ nguyên)
        Map<Integer, Map<Integer, Double>> memory =
                buildGlobalMemory(allResults, latestDate);

        Map<Integer, List<NumberScoreDto>> result =
                convertMemoryToResult(memory);

        // 🔥 2. CONTEXT (mới)
        Result today = allResults.get(allResults.size() - 1);

        List<NumberScoreDto> contextResult =
                applyTodayContext(memory, today);

        result.put(-1, contextResult); // 🔥 inject vào

        return result;
    }

    // =========================================================
    // GLOBAL MEMORY (KHÔNG ĐỔI)
    // =========================================================

    public Map<Integer, Map<Integer, Double>> buildGlobalMemory(
            List<Result> allResults,
            LocalDate latestDate
    ) {

        Map<Integer, Map<Integer, Double>> memory = new HashMap<>();

        for (int i = 0; i < 10; i++) {
            Map<Integer, Double> inner = new HashMap<>();
            for (int j = 0; j < 10; j++) {
                inner.put(j, 0.0);
            }
            memory.put(i, inner);
        }

        for (int i = 0; i < allResults.size() - 1; i++) {

            Result today = allResults.get(i);
            Result next = allResults.get(i + 1);

            long days = ChronoUnit.DAYS.between(today.getDate(), latestDate);
            double weight = getWeight(days);
            if (weight == 0) continue;

            Set<Integer> todaySet = toSet(today);
            Set<Integer> nextSet = toSet(next);

            for (int a : todaySet) {
                int groupA = a / GROUP_SIZE;

                for (int b : nextSet) {
                    int groupB = b / GROUP_SIZE;

                    memory.get(groupA)
                            .put(groupB, memory.get(groupA).get(groupB) + weight);
                }
            }
        }

        return memory;
    }

    // =========================================================
    // CONVERT (KHÔNG ĐỔI)
    // =========================================================

    public Map<Integer, List<NumberScoreDto>> convertMemoryToResult(
            Map<Integer, Map<Integer, Double>> memory
    ) {

        Map<Integer, List<NumberScoreDto>> result = new HashMap<>();

        for (int groupA = 0; groupA < 10; groupA++) {

            Map<Integer, Double> groupScores = memory.get(groupA);
            List<NumberScoreDto> list = new ArrayList<>();

            for (int n = 0; n < 100; n++) {
                int groupB = n / GROUP_SIZE;
                double score = groupScores.get(groupB);
                list.add(new NumberScoreDto(n, score));
            }

            list.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));

            result.put(groupA, list);
        }

        return result;
    }

    // =========================================================
    // CONTEXT (MỚI)
    // =========================================================

    public List<NumberScoreDto> applyTodayContext(
            Map<Integer, Map<Integer, Double>> memory,
            Result today
    ) {

        Set<Integer> todaySet = toSet(today);

        Map<Integer, Double> voteMap = new HashMap<>();

        for (int todayNumber : todaySet) {

            int groupA = todayNumber / GROUP_SIZE;

            Map<Integer, Double> groupScores = memory.get(groupA);
            if (groupScores == null) continue;

            for (int target = 0; target < 100; target++) {

                int groupB = target / GROUP_SIZE;

                double score = groupScores.getOrDefault(groupB, 0.0);

                voteMap.put(
                        target,
                        voteMap.getOrDefault(target, 0.0) + score
                );
            }
        }

        return voteMap.entrySet()
                .stream()
                .map(e -> new NumberScoreDto(e.getKey(), e.getValue()))
                .sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
                .toList();
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

    private double getWeight(long days) {
        if (days <= 30) return W_30D;
        if (days <= 90) return W_90D;
        if (days <= 180) return W_180D;
        return 0;
    }
}