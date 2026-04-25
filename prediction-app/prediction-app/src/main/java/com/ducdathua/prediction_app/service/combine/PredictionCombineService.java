package com.ducdathua.prediction_app.service.combine;

import com.ducdathua.prediction_app.dto.CombineResponseDto;
import com.ducdathua.prediction_app.dto.NumberScoreDto;
import com.ducdathua.prediction_app.model.Result;
import com.ducdathua.prediction_app.service.predictor.Predictor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PredictionCombineService {

    private final List<Predictor> predictors;

    public PredictionCombineService(List<Predictor> predictors) {
        this.predictors = predictors;
    }

    public CombineResponseDto combine(List<Result> allResults) {

        // 🔥 context hôm nay
        Result today = allResults.get(allResults.size() - 1);
        Set<Integer> todaySet = new HashSet<>(today.getNumbers());
        todaySet.add(today.getSingleNumber());

        Map<Integer, Double> scoreMap = initZeroMap();
        Map<Integer, Double> penaltyMap = initOneMap();
        Map<Integer, Integer> voteMap = new HashMap<>();

        for (Predictor p : predictors) {

            Map<Integer, List<NumberScoreDto>> raw = p.predict(allResults);

            Map<Integer, Double> flat =
                    isContextPredictor(p.getName())
                            ? extractWithContext(raw, todaySet)
                            : extractGlobal(raw);

            flat = normalize(flat);

            // 🔥 STREAK = penalty riêng
            if (p.getName().equals("STREAK_BREAK")) {

                for (int i = 0; i < 100; i++) {
                    double val = flat.getOrDefault(i, 1.0);
                    penaltyMap.put(i,
                            penaltyMap.get(i) * (0.5 + 0.5 * val));
                }

            } else {

                double weight = getWeight(p.getName());

                for (int i = 0; i < 100; i++) {
                    double val = flat.getOrDefault(i, 0.0);
                    scoreMap.put(i,
                            scoreMap.get(i) + val * weight);
                }
            }

            // 🔥 vote (context-aware)
            List<NumberScoreDto> top =
                    raw.containsKey(-1)
                            ? raw.get(-1).subList(0, Math.min(10, raw.get(-1).size()))
                            : extractTopFromContext(raw, todaySet);

            for (NumberScoreDto dto : top) {
                voteMap.put(dto.getNumber(),
                        voteMap.getOrDefault(dto.getNumber(), 0) + 1);
            }
        }

        // 🔥 combine score + penalty
        Map<Integer, Double> finalMap = new HashMap<>();

        for (int i = 0; i < 100; i++) {
            double score = scoreMap.get(i) * penaltyMap.get(i);

            // 🔥 agreement boost
            int vote = voteMap.getOrDefault(i, 0);
            score += vote * 0.02;

            finalMap.put(i, score);
        }

        // 🔥 temperature
        applyTemperature(finalMap, 0.7);

        // 🔥 normalize cuối
        finalMap = normalize(finalMap);

        List<NumberScoreDto> sorted = toSortedList(finalMap);

        return new CombineResponseDto(sorted);
    }

    // =========================
    // EXTRACT
    // =========================

    private Map<Integer, Double> extractGlobal(Map<Integer, List<NumberScoreDto>> raw) {

        Map<Integer, Double> map = initZeroMap();

        if (raw.containsKey(-1)) {
            for (NumberScoreDto dto : raw.get(-1)) {
                map.put(dto.getNumber(), dto.getScore());
            }
        }

        return map;
    }

    private Map<Integer, Double> extractWithContext(
            Map<Integer, List<NumberScoreDto>> raw,
            Set<Integer> todaySet
    ) {

        Map<Integer, Double> map = initZeroMap();

        for (Integer n : todaySet) {
            List<NumberScoreDto> next = raw.get(n);
            if (next == null) continue;

            for (NumberScoreDto dto : next) {
                map.put(dto.getNumber(),
                        map.get(dto.getNumber()) + dto.getScore());
            }
        }

        return map;
    }

    private List<NumberScoreDto> extractTopFromContext(
            Map<Integer, List<NumberScoreDto>> raw,
            Set<Integer> todaySet
    ) {

        Map<Integer, Double> map = new HashMap<>();

        for (Integer n : todaySet) {
            List<NumberScoreDto> list = raw.get(n);
            if (list == null) continue;

            for (NumberScoreDto dto : list) {
                map.put(dto.getNumber(),
                        map.getOrDefault(dto.getNumber(), 0.0) + dto.getScore());
            }
        }

        return map.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(10)
                .map(e -> new NumberScoreDto(e.getKey(), e.getValue()))
                .toList();
    }

    // =========================
    // HELPERS
    // =========================

    private boolean isContextPredictor(String name) {
        return name.equals("PAIR")
                || name.equals("TIME_WEIGHTED_COUNT")
                || name.equals("POSITION");
    }

    private Map<Integer, Double> normalize(Map<Integer, Double> map) {

        double sum = map.values().stream().mapToDouble(d -> d).sum();

        Map<Integer, Double> res = new HashMap<>();

        for (int i = 0; i < 100; i++) {
            double v = map.getOrDefault(i, 0.0);
            res.put(i, v / (sum + 1e-9));
        }

        return res;
    }

    private void applyTemperature(Map<Integer, Double> map, double temp) {
        for (int i = 0; i < 100; i++) {
            double v = map.get(i);
            map.put(i, Math.pow(v, 1.0 / temp));
        }
    }

    private double getWeight(String name) {

        return switch (name) {
            case "RECENT_FREQUENCY" -> 0.25;
            case "REPEAT" -> 0.15;
            case "GAP" -> 0.15;

            case "PAIR" -> 0.15;
            case "TIME_WEIGHTED_COUNT" -> 0.1;
            case "POSITION" -> 0.1;

            default -> 0.1;
        };
    }

    private Map<Integer, Double> initZeroMap() {
        Map<Integer, Double> m = new HashMap<>();
        for (int i = 0; i < 100; i++) m.put(i, 0.0);
        return m;
    }

    private Map<Integer, Double> initOneMap() {
        Map<Integer, Double> m = new HashMap<>();
        for (int i = 0; i < 100; i++) m.put(i, 1.0);
        return m;
    }

    private List<NumberScoreDto> toSortedList(Map<Integer, Double> map) {
        return map.entrySet()
                .stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .map(e -> new NumberScoreDto(e.getKey(), e.getValue()))
                .toList();
    }
}