package com.ducdathua.prediction_app.service.predictor;

import com.ducdathua.prediction_app.dto.NumberScoreDto;
import com.ducdathua.prediction_app.model.Result;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class PositionPredictor implements Predictor {

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

        // ===== SORT =====
        allResults.sort(Comparator.comparing(Result::getDate));
        LocalDate latestDate = allResults.get(allResults.size() - 1).getDate();

        // ===== BUILD MEMORY: group → group =====
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

            Set<Integer> todaySet = new HashSet<>(today.getNumbers());
            Set<Integer> nextSet = new HashSet<>(next.getNumbers());

            for (int a : todaySet) {
                int groupA = a / GROUP_SIZE;

                for (int b : nextSet) {
                    int groupB = b / GROUP_SIZE;

                    memory.get(groupA)
                            .put(groupB, memory.get(groupA).get(groupB) + weight);
                }
            }
        }

        // ===== CONVERT: group → number =====
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
//        System.out.println(memory);
        return result;
    }

    // ===== WEIGHT =====
    private double getWeight(long days) {
        if (days <= 30) return W_30D;
        if (days <= 90) return W_90D;
        if (days <= 180) return W_180D;
        return 0;
    }
}