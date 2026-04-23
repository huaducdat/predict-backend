package com.ducdathua.prediction_app.service.chain;

import com.ducdathua.prediction_app.model.Result;
import com.ducdathua.prediction_app.service.PredictionChain;
import com.ducdathua.prediction_app.service.util.PredictionMath;
import com.ducdathua.prediction_app.service.util.TimeWindowHelper;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class TransitionChain implements PredictionChain {

    @Override
    public String getName() {
        return "TransitionChain";
    }

    @Override
    public double getWeight() {
        return 0.35;
    }

    @Override
    public Map<Integer, Double> calculateScores(List<Result> allResults) {
        List<Result> sorted = TimeWindowHelper.sortByDateAsc(allResults);
        Map<Integer, Double> raw = PredictionMath.initZeroMap();

        if (sorted.size() < 2) {
            return raw;
        }

        Result latest = sorted.get(sorted.size() - 1);
        Set<Integer> latestNumbers = new HashSet<>(latest.getNumbers());

        for (int i = 0; i < sorted.size() - 1; i++) {
            Result current = sorted.get(i);
            Result next = sorted.get(i + 1);

            boolean matched = false;
            for (Integer currentNumber : current.getNumbers()) {
                if (latestNumbers.contains(currentNumber)) {
                    matched = true;
                    break;
                }
            }

            if (!matched) continue;

            for (Integer nextNumber : next.getNumbers()) {
                raw.put(nextNumber, raw.get(nextNumber) + 1.0);
            }
        }

        return PredictionMath.normalize(raw);
    }
}