package com.ducdathua.prediction_app.service.util;

import com.ducdathua.prediction_app.dto.NumberScoreDto;

import java.util.*;
import java.util.stream.Collectors;

public final class PredictionMath {
    private PredictionMath() {
    }

    public static Map<Integer, Double> initZeroMap() {
        Map<Integer, Double> map = new LinkedHashMap<>();
        for (int i = 0; i < 100; i++) {
            map.put(i, 0.0);
        }
        return map;
    }

    public static Map<Integer, Double> normalize(Map<Integer, Double> raw) {
        double max = raw.values().stream().mapToDouble(Double::doubleValue).max().orElse(0.0);

        Map<Integer, Double> normalized = new LinkedHashMap<>();
        if (max <= 0.0) {
            raw.keySet().forEach(k -> normalized.put(k, 0.0));
            return normalized;
        }

        raw.forEach((k, v) -> normalized.put(k, v / max));
        return normalized;
    }

    public static List<NumberScoreDto> toSortedDtoList(Map<Integer, Double> scoreMap) {
        double total = scoreMap.values().stream().mapToDouble(Double::doubleValue).sum();

        return scoreMap.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .map(entry -> {
                    double percentage = total <= 0 ? 0.0 : (entry.getValue() / total) * 100.0;
                    return new NumberScoreDto(entry.getKey(), round(entry.getValue()), round(percentage));
                })
                .collect(Collectors.toList());
    }

    public static Map<Integer, Double> addWeighted(Map<Integer, Double> base,
                                                   Map<Integer, Double> addition,
                                                   double weight) {
        Map<Integer, Double> result = new LinkedHashMap<>(base);
        for (int i = 0; i < 100; i++) {
            double current = result.getOrDefault(i, 0.0);
            double extra = addition.getOrDefault(i, 0.0) * weight;
            result.put(i, current + extra);
        }
        return result;
    }

    public static double round(double value) {
        return Math.round(value * 10000.0) / 10000.0;
    }
}