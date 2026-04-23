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
public class CycleChain implements PredictionChain {

    @Override
    public String getName() {
        return "Cycle";
    }

    @Override
    public double getWeight() {
        return 0.1;
    }

    @Override
    public Map<Integer, Double> calculateScores(List<Result> results) {

        Map<Integer, Double> raw = PredictionMath.initZeroMap();
        List<Result> sorted = TimeWindowHelper.sortByDateAsc(results);

        Map<Integer, Integer> lastSeen = new HashMap<>();

        for (int i = 0; i < sorted.size(); i++) {
            Result r = sorted.get(i);

            for (Integer n : r.getNumbers()) {
                if (lastSeen.containsKey(n)) {
                    int gap = i - lastSeen.get(n);

                    if (gap > 0 && gap <= 10) {
                        raw.put(n, raw.get(n) + (1.0 / gap));
                    }
                }
                lastSeen.put(n, i);
            }
        }

        return PredictionMath.normalize(raw);
    }
}