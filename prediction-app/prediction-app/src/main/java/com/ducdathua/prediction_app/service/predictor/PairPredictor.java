package com.ducdathua.prediction_app.service.predictor;

import com.ducdathua.prediction_app.dto.NumberScoreDto;
import com.ducdathua.prediction_app.dto.PairScoreDto;
import com.ducdathua.prediction_app.model.Result;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class PairPredictor implements Predictor<List<PairScoreDto>> {

    private static final double W_7D  = 0.4;
    private static final double W_30D = 0.3;
    private static final double W_90D = 0.2;
    private static final double W_180D = 0.1;

    @Override
    public String getName() {
        return "PAIR_TO_NEXT";
    }

    @Override
    public List<PairScoreDto> predict(List<Result> allResults) {

        if (allResults == null || allResults.size() < 2) {
            return List.of(); // 🔥 FIX: không return Map nữa
        }

        allResults.sort(Comparator.comparing(Result::getDate));

        LocalDate latestDate =
                allResults.get(allResults.size() - 1).getDate();

        // 🔥 pairKey → (target → score)
        Map<Integer, Map<Integer, Double>> memory = new HashMap<>();

        for (int i = 0; i < allResults.size() - 1; i++) {

            Result today = allResults.get(i);
            Result next = allResults.get(i + 1);

            long daysDiff = ChronoUnit.DAYS.between(today.getDate(), latestDate);
            double weight = getWeight(daysDiff);
            if (weight == 0) continue;

            Set<Integer> todaySet = toSet(today);
            Set<Integer> nextSet = toSet(next);

            List<Integer> list = new ArrayList<>(todaySet);

            // 🔥 tạo cặp (A,B)
            for (int a = 0; a < list.size(); a++) {
                for (int b = a + 1; b < list.size(); b++) {

                    int A = list.get(a);
                    int B = list.get(b);

                    int pairKey = encodePair(A, B);

                    memory.putIfAbsent(pairKey, new HashMap<>());
                    Map<Integer, Double> freq = memory.get(pairKey);

                    for (Integer target : nextSet) {
                        freq.put(target,
                                freq.getOrDefault(target, 0.0) + weight);
                    }
                }
            }
        }

        // 🔥 convert → List<PairScoreDto>
        return memory.entrySet()
                .stream()
                .map(entry -> {

                    int pairKey = entry.getKey();
                    Map<Integer, Double> freq = entry.getValue();

                    // 🔥 tính score tổng của pair
                    double totalScore = freq.values()
                            .stream()
                            .mapToDouble(Double::doubleValue)
                            .sum();

                    // 🔥 sort target numbers
                    List<NumberScoreDto> numbers = freq.entrySet()
                            .stream()
                            .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                            .limit(5)
                            .map(e -> new NumberScoreDto(e.getKey(), e.getValue()))
                            .toList();

                    return new PairScoreDto(pairKey, totalScore, numbers);
                })
                // 🔥 sort pair theo score
                .sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
                .toList();
    }

    // ===== encode pair =====

    private int encodePair(int a, int b) {
        int min = Math.min(a, b);
        int max = Math.max(a, b);
        return min * 100 + max;
    }

    // ===== helpers =====

    private Set<Integer> toSet(Result r) {
        Set<Integer> set = new HashSet<>(r.getNumbers());
        set.add(r.getSingleNumber());
        return set;
    }

    private double getWeight(long daysDiff) {
        if (daysDiff <= 7) return W_7D;
        if (daysDiff <= 30) return W_30D;
        if (daysDiff <= 90) return W_90D;
        if (daysDiff <= 180) return W_180D;
        return 0;
    }
}