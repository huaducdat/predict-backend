package com.ducdathua.prediction_app.service.chain;
import com.ducdathua.prediction_app.model.Result;
import com.ducdathua.prediction_app.service.PredictionChain;
import com.ducdathua.prediction_app.service.util.PredictionMath;
import com.ducdathua.prediction_app.service.util.TimeWindowHelper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TrueTransitionChain implements PredictionChain {

    @Override
    public String getName() {
        return "TrueTransition";
    }

    @Override
    public double getWeight() {
        return 0.4;
    }

    @Override
    public Map<Integer, Double> calculateScores(List<Result> results) {

        Map<Integer, Double> raw = PredictionMath.initZeroMap();
        if (results.size() < 2) return raw;

        List<Result> sorted = TimeWindowHelper.sortByDateAsc(results);

        Map<Integer, Map<Integer, Integer>> transitionMap = buildTransitionMap(sorted);

        int current = sorted.get(sorted.size() - 1).getSingleNumber();

        Map<Integer, Integer> nextMap = transitionMap.getOrDefault(current, Map.of());

        nextMap.forEach((k, v) -> raw.put(k, v.doubleValue()));

        return PredictionMath.normalize(raw);
    }

    private Map<Integer, Map<Integer, Integer>> buildTransitionMap(List<Result> results) {
        Map<Integer, Map<Integer, Integer>> map = new HashMap<>();

        for (int i = 0; i < results.size() - 1; i++) {
            int x = results.get(i).getSingleNumber();
            int y = results.get(i + 1).getSingleNumber();

            map.computeIfAbsent(x, k -> new HashMap<>())
                    .merge(y, 1, Integer::sum);
        }

        return map;
    }
}