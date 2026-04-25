package com.ducdathua.prediction_app.service.predictor;

import com.ducdathua.prediction_app.dto.NumberScoreDto;
import com.ducdathua.prediction_app.model.Result;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class GapPredictor implements Predictor<Map<Integer, List<NumberScoreDto>>>  {

    private static final int MAX_GAP = 180;

    private static final double W_90D = 0.4;
    private static final double W_180D = 0.6;

    @Override
    public String getName() {
        return "GAP";
    }

    @Override
    public Map<Integer, List<NumberScoreDto>> predict(List<Result> allResults) {

        if (allResults == null || allResults.isEmpty()) {
            return Map.of();
        }

        allResults.sort(Comparator.comparing(Result::getDate));

        LocalDate latestDate = allResults.get(allResults.size() - 1).getDate();

        Map<Integer, LocalDate> lastSeen = new HashMap<>();

        for (Result r : allResults) {
            LocalDate date = r.getDate();
            Set<Integer> numbers = new HashSet<>(r.getNumbers());

            for (Integer n : numbers) {
                lastSeen.put(n, date);
            }
        }

        Map<Integer, Double> scoreMap = new HashMap<>();

        for (int i = 0; i < 100; i++) {

            double gap;

            if (!lastSeen.containsKey(i)) {
                gap = MAX_GAP;
            } else {
                gap = ChronoUnit.DAYS.between(lastSeen.get(i), latestDate);
            }

            // 🔥 phân cấp
            double score =
                    Math.min(gap, 90)  * W_90D +
                            Math.min(gap, 180) * W_180D;

            scoreMap.put(i, score);
        }

        List<NumberScoreDto> sorted = scoreMap.entrySet()
                .stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .map(e -> new NumberScoreDto(e.getKey(), e.getValue()))
                .toList();

        Map<Integer, List<NumberScoreDto>> result = new HashMap<>();
        result.put(-1, sorted);

        return result;
    }
}