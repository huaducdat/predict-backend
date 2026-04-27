package com.ducdathua.prediction_app.service.util;

import com.ducdathua.prediction_app.dto.NumberScoreDto;

import java.util.*;

public class PredictionMath {

    // =========================================================
    // 1. LIST → MAP
    // =========================================================

    public static Map<Integer, Double> toMap(List<NumberScoreDto> list) {

        Map<Integer, Double> map = new HashMap<>();

        if (list == null) return map;

        for (NumberScoreDto dto : list) {
            map.put(dto.getNumber(), dto.getScore());
        }

        return map;
    }

    // =========================================================
    // 2. NORMALIZE BY SUM
    // =========================================================

    public static Map<Integer, Double> normalizeBySum(Map<Integer, Double> map) {

        double sum = map.values()
                .stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        Map<Integer, Double> result = new HashMap<>();

        for (Map.Entry<Integer, Double> e : map.entrySet()) {

            double value = (sum == 0)
                    ? 0
                    : e.getValue() / sum;

            result.put(e.getKey(), value);
        }

        return result;
    }

    // =========================================================
    // 3. ADD WEIGHTED
    // =========================================================

    public static Map<Integer, Double> addWeighted(
            Map<Integer, Double> base,
            Map<Integer, Double> incoming,
            double weight
    ) {

        for (Map.Entry<Integer, Double> e : incoming.entrySet()) {

            int number = e.getKey();
            double score = e.getValue() * weight;

            base.put(
                    number,
                    base.getOrDefault(number, 0.0) + score
            );
        }

        return base;
    }

    // =========================================================
    // 4. INIT ZERO MAP (00–99)
    // =========================================================

    public static Map<Integer, Double> initZeroMap() {

        Map<Integer, Double> map = new HashMap<>();

        for (int i = 0; i < 100; i++) {
            map.put(i, 0.0);
        }

        return map;
    }

    // =========================================================
    // 5. MAP → SORTED DTO
    // =========================================================

    public static List<NumberScoreDto> toSortedDtoList(Map<Integer, Double> map) {

        return map.entrySet()
                .stream()
                .map(e -> new NumberScoreDto(e.getKey(), e.getValue()))
                .sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
                .toList();
    }
}