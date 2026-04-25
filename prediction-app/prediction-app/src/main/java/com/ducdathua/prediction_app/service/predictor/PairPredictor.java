package com.ducdathua.prediction_app.service.predictor;

import com.ducdathua.prediction_app.dto.NumberScoreDto;
import com.ducdathua.prediction_app.model.Result;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class PairPredictor implements Predictor {

    private static final double W_30D = 0.5;
    private static final double W_90D = 0.3;
    private static final double W_180D = 0.2;

    @Override
    public String getName() {
        return "PAIR";
    }

    @Override
    public Map<Integer, List<NumberScoreDto>> predict(List<Result> allResults) {

        if (allResults == null || allResults.size() < 2) {
            return Map.of();
        }

        allResults.sort(Comparator.comparing(Result::getDate));

        LocalDate latestDate = allResults.get(allResults.size() - 1).getDate();

        // 🔥 memory: A -> (B -> score)
        Map<Integer, Map<Integer, Double>> memory = initMemory();

        for (int i = 0; i < allResults.size() - 1; i++) {

            Result today = allResults.get(i);
            Result next = allResults.get(i + 1);

            LocalDate date = today.getDate();
            long daysDiff = ChronoUnit.DAYS.between(date, latestDate);

            double weight = getWeight(daysDiff);
            if (weight == 0) continue;

            Set<Integer> todaySet = new HashSet<>(today.getNumbers());
            Set<Integer> nextSet = new HashSet<>(next.getNumbers());

            for (Integer A : todaySet) {
                Map<Integer, Double> freq = memory.get(A);

                for (Integer B : nextSet) {
                    freq.put(B, freq.getOrDefault(B, 0.0) + weight);
                }
            }
        }

        // 🔥 convert thành output
        Map<Integer, List<NumberScoreDto>> result = new HashMap<>();

        for (int A = 0; A < 100; A++) {

            Map<Integer, Double> freq = memory.get(A);

            List<NumberScoreDto> sorted = freq.entrySet()
                    .stream()
                    .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                    .map(e -> new NumberScoreDto(e.getKey(), e.getValue()))
                    .toList();

            result.put(A, sorted);
        }

        return result;
    }

    private Map<Integer, Map<Integer, Double>> initMemory() {
        Map<Integer, Map<Integer, Double>> m = new HashMap<>();

        for (int i = 0; i < 100; i++) {
            Map<Integer, Double> inner = new HashMap<>();
            for (int j = 0; j < 100; j++) {
                inner.put(j, 0.0);
            }
            m.put(i, inner);
        }

        return m;
    }

    private double getWeight(long daysDiff) {
        if (daysDiff <= 30) return W_30D;
        if (daysDiff <= 90) return W_90D;
        if (daysDiff <= 180) return W_180D;
        return 0;
    }
}