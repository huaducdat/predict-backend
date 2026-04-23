package com.ducdathua.prediction_app.service.chain;

import com.ducdathua.prediction_app.model.Result;
import com.ducdathua.prediction_app.service.PredictionChain;
import com.ducdathua.prediction_app.service.util.PredictionMath;
import com.ducdathua.prediction_app.service.util.TimeWindowHelper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class FrequencyChain implements PredictionChain {

    @Override
    public String getName() {
        return "FrequencyChain";
    }

    @Override
    public double getWeight() {
        return 0.20;
    }

    @Override
    public Map<Integer, Double> calculateScores(List<Result> allResults) {
        Map<Integer, Double> score30 = calcWindow(TimeWindowHelper.filterLastDays(allResults, 30));
        Map<Integer, Double> score90 = calcWindow(TimeWindowHelper.filterLastDays(allResults, 90));
        Map<Integer, Double> score180 = calcWindow(TimeWindowHelper.filterLastDays(allResults, 180));

        Map<Integer, Double> finalMap = PredictionMath.initZeroMap();
        finalMap = PredictionMath.addWeighted(finalMap, score30, 0.6);
        finalMap = PredictionMath.addWeighted(finalMap, score90, 0.3);
        finalMap = PredictionMath.addWeighted(finalMap, score180, 0.1);

        return PredictionMath.normalize(finalMap);
    }

    private Map<Integer, Double> calcWindow(List<Result> results) {
        Map<Integer, Double> raw = PredictionMath.initZeroMap();

        for (Result result : results) {
            if (result.getNumbers() == null) continue;
            for (Integer number : result.getNumbers()) {
                raw.put(number, raw.get(number) + 1.0);
            }
        }

        return PredictionMath.normalize(raw);
    }
}