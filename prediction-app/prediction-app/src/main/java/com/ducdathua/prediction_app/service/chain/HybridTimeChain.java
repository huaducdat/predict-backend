package com.ducdathua.prediction_app.service.chain;

import com.ducdathua.prediction_app.model.Result;
import com.ducdathua.prediction_app.service.PredictionChain;
import com.ducdathua.prediction_app.service.util.PredictionMath;
import com.ducdathua.prediction_app.service.util.TimeWindowHelper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class HybridTimeChain implements PredictionChain {

    @Override
    public String getName() {
        return "HybridTimeChain";
    }

    @Override
    public double getWeight() {
        return 0.20;
    }

    @Override
    public Map<Integer, Double> calculateScores(List<Result> allResults) {
        Map<Integer, Double> s30 = buildLayer(TimeWindowHelper.filterLastDays(allResults, 30));
        Map<Integer, Double> s90 = buildLayer(TimeWindowHelper.filterLastDays(allResults, 90));
        Map<Integer, Double> s180 = buildLayer(TimeWindowHelper.filterLastDays(allResults, 180));

        Map<Integer, Double> finalMap = PredictionMath.initZeroMap();
        finalMap = PredictionMath.addWeighted(finalMap, s30, 0.6);
        finalMap = PredictionMath.addWeighted(finalMap, s90, 0.3);
        finalMap = PredictionMath.addWeighted(finalMap, s180, 0.1);

        return PredictionMath.normalize(finalMap);
    }

    private Map<Integer, Double> buildLayer(List<Result> results) {
        Map<Integer, Double> frequency = PredictionMath.initZeroMap();
        Map<Integer, Double> streakLike = PredictionMath.initZeroMap();

        for (Result result : results) {
            for (Integer n : result.getNumbers()) {
                frequency.put(n, frequency.get(n) + 1.0);
            }
        }

        List<Result> sorted = TimeWindowHelper.sortByDateAsc(results);
        for (int number = 0; number < 100; number++) {
            int count = 0;
            for (int i = sorted.size() - 1; i >= 0; i--) {
                if (sorted.get(i).getNumbers().contains(number)) {
                    count++;
                } else {
                    break;
                }
            }
            streakLike.put(number, (double) count);
        }

        frequency = PredictionMath.normalize(frequency);
        streakLike = PredictionMath.normalize(streakLike);

        Map<Integer, Double> layer = PredictionMath.initZeroMap();
        layer = PredictionMath.addWeighted(layer, frequency, 0.7);
        layer = PredictionMath.addWeighted(layer, streakLike, 0.3);

        return PredictionMath.normalize(layer);
    }
}