package com.ducdathua.prediction_app.service.chain;

import com.ducdathua.prediction_app.model.Result;
import com.ducdathua.prediction_app.service.PredictionChain;
import com.ducdathua.prediction_app.service.util.PredictionMath;
import com.ducdathua.prediction_app.service.util.TimeWindowHelper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class GlobalHotChain implements PredictionChain {

    @Override
    public String getName() {
        return "GlobalHotChain";
    }

    @Override
    public double getWeight() {
        return 0.10;
    }

    @Override
    public Map<Integer, Double> calculateScores(List<Result> allResults) {
        List<Result> results = TimeWindowHelper.filterLastDays(allResults, 180);
        Map<Integer, Double> raw = PredictionMath.initZeroMap();

        for (Result result : results) {
            for (Integer number : result.getNumbers()) {
                raw.put(number, raw.get(number) + 1.0);
            }
        }

        return PredictionMath.normalize(raw);
    }
}