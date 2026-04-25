package com.ducdathua.prediction_app.service.predictor;

import com.ducdathua.prediction_app.dto.NumberScoreDto;
import com.ducdathua.prediction_app.model.Result;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class RecentFrequencyPredictor implements Predictor {

    private static final int TOP_K = 15;

    private static final double W_7D  = 0.4;
    private static final double W_30D = 0.3;
    private static final double W_90D = 0.2;
    private static final double W_180D = 0.1;

    @Override
    public String getName() {
        return "RECENT_FREQUENCY";
    }

    @Override
    public Map<Integer, List<NumberScoreDto>> predict(List<Result> allResults) {

        if (allResults == null || allResults.isEmpty()) {
            return Map.of();
        }

        allResults.sort(Comparator.comparing(Result::getDate));

        LocalDate latestDate = allResults.get(allResults.size() - 1).getDate();

        // 🔥 score cho từng số 0–99
        Map<Integer, Double> scoreMap = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            scoreMap.put(i, 0.0);
        }

        // 🔥 duyệt toàn bộ lịch sử
        for (Result r : allResults) {

            LocalDate date = r.getDate();
            long daysDiff = ChronoUnit.DAYS.between(date, latestDate);

            double weight = getWeight(daysDiff);

            if (weight == 0) continue;

            Set<Integer> numbers = toSet(r);

            for (Integer n : numbers) {
                scoreMap.put(n, scoreMap.get(n) + weight);
            }
        }

        // 🔥 sort toàn bộ 0–99 theo score
        List<NumberScoreDto> sorted = scoreMap.entrySet()
                .stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .map(e -> new NumberScoreDto(e.getKey(), e.getValue()))
                .toList();

        // 🔥 trả top K dưới key đặc biệt (-1)
        Map<Integer, List<NumberScoreDto>> result = new HashMap<>();
        result.put(-1, sorted); // 🔥 bỏ subList

        return result;
    }

    private double getWeight(long daysDiff) {
        if (daysDiff <= 7) return W_7D;
        if (daysDiff <= 30) return W_30D;
        if (daysDiff <= 90) return W_90D;
        if (daysDiff <= 180) return W_180D;
        return 0.05;
    }

    private Set<Integer> toSet(Result r) {
        Set<Integer> set = new HashSet<>(r.getNumbers());
        set.add(r.getSingleNumber());
        return set;
    }
}