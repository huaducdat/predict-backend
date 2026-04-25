package com.ducdathua.prediction_app.service.predictor;

import com.ducdathua.prediction_app.dto.NumberScoreDto;
import com.ducdathua.prediction_app.model.Result;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
@Component
public class RepeatPredictor implements Predictor<Map<Integer, List<NumberScoreDto>>>  {

    private static final double W_30D = 0.5;
    private static final double W_90D = 0.3;
    private static final double W_180D = 0.2;

    @Override
    public String getName() {
        return "REPEAT";
    }

    @Override
    public Map<Integer, List<NumberScoreDto>> predict(List<Result> allResults) {

        if (allResults == null || allResults.size() < 2) {
            return Map.of();
        }

        allResults.sort(Comparator.comparing(Result::getDate));

        LocalDate latestDate = allResults.get(allResults.size() - 1).getDate();

        Map<Integer, Double> scoreMap = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            scoreMap.put(i, 0.0);
        }

        for (int i = 0; i < allResults.size() - 1; i++) {

            Result today = allResults.get(i);
            Result next = allResults.get(i + 1);

            LocalDate date = today.getDate();
            long daysDiff = ChronoUnit.DAYS.between(date, latestDate);

            double weight = getWeight(daysDiff);
            if (weight == 0) continue;

            Set<Integer> todaySet = new HashSet<>(today.getNumbers());
            Set<Integer> nextSet = new HashSet<>(next.getNumbers());

            for (Integer n : todaySet) {

                if (nextSet.contains(n)) {
                    scoreMap.put(n, scoreMap.get(n) + weight);
                }
            }
        }

        // sort full 100
        List<NumberScoreDto> sorted = scoreMap.entrySet()
                .stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .map(e -> new NumberScoreDto(e.getKey(), e.getValue()))
                .toList();

        Map<Integer, List<NumberScoreDto>> result = new HashMap<>();
        result.put(-1, sorted);

        return result;
    }

    private double getWeight(long daysDiff) {
        if (daysDiff <= 30) return W_30D;
        if (daysDiff <= 90) return W_90D;
        if (daysDiff <= 180) return W_180D;
        return 0;
    }
}