package com.ducdathua.prediction_app.service.predictor;

import com.ducdathua.prediction_app.dto.NumberScoreDto;
import com.ducdathua.prediction_app.model.Result;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class TimeWeightedScorePredictor implements Predictor<Map<Integer, List<NumberScoreDto>>>  {

    private static final int TOP_K = 3;

    // 🔥 trọng số (ông có thể chỉnh)
    private static final double W_7D  = 0.4;
    private static final double W_30D = 0.3;
    private static final double W_90D = 0.2;
    private static final double W_180D = 0.1;

    @Override
    public String getName() {
        return "TIME_WEIGHTED_COUNT";
    }

    @Override
    public Map<Integer, List<NumberScoreDto>> predict(List<Result> allResults) {

        if (allResults == null || allResults.size() < 2) {
            return emptyResult();
        }

        // 🔥 sort theo ngày
        allResults.sort(Comparator.comparing(Result::getDate));

        // 🔥 lấy ngày cuối
        LocalDate latestDate =
                allResults.get(allResults.size() - 1).getDate();


        // 🔥 memory final
        Map<Integer, Map<Integer, Double>> memory = initMemory();

        for (int i = 0; i < allResults.size() - 1; i++) {

            Result today = allResults.get(i);
            Result next = allResults.get(i + 1);

            LocalDate date = today.getDate();

            long daysDiff = ChronoUnit.DAYS.between(date, latestDate);

            double weight = getWeight(daysDiff);

            if (weight == 0) continue;

            Set<Integer> todaySet = toSet(today);
            Set<Integer> nextSet = toSet(next);

            for (Integer source : todaySet) {
                Map<Integer, Double> freq = memory.get(source);

                for (Integer target : nextSet) {
                    freq.put(target, freq.getOrDefault(target, 0.0) + weight);
                }
            }
        }

        // 🔥 sort + lấy top3
        Map<Integer, List<NumberScoreDto>> result = new HashMap<>();

        for (int s = 0; s < 100; s++) {

            Map<Integer, Double> freq = memory.get(s);

            if (freq.isEmpty()) {
                result.put(s, List.of());
                continue;
            }

            List<NumberScoreDto> top = freq.entrySet()
                    .stream()
                    .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                    .limit(TOP_K)
                    .map(e -> new NumberScoreDto(
                            e.getKey(),
                            e.getValue() // 🔥 giữ nguyên score
                    ))
                    .toList();

            result.put(s, top);
        }

        return result;
    }

    // ===== weight logic =====

    private double getWeight(long daysDiff) {

        if (daysDiff <= 7) return W_7D;
        if (daysDiff <= 30) return W_30D;
        if (daysDiff <= 90) return W_90D;
        if (daysDiff <= 180) return W_180D;

        return 0;
    }

    // ===== helpers =====

    private Map<Integer, Map<Integer, Double>> initMemory() {
        Map<Integer, Map<Integer, Double>> m = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            m.put(i, new HashMap<>());
        }
        return m;
    }

    private Set<Integer> toSet(Result r) {
        Set<Integer> set = new HashSet<>(r.getNumbers());
        set.add(r.getSingleNumber());
        return set;
    }

    private Map<Integer, List<NumberScoreDto>> emptyResult() {
        Map<Integer, List<NumberScoreDto>> r = new HashMap<>();
        for (int i = 0; i < 100; i++) r.put(i, List.of());
        return r;
    }
}