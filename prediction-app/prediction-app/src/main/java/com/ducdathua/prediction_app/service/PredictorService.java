package com.ducdathua.prediction_app.service;

import com.ducdathua.prediction_app.model.Result;
import com.ducdathua.prediction_app.repository.ResultRepository;
import com.ducdathua.prediction_app.service.predictor.Predictor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PredictorService {

    private final ResultRepository resultRepository;
    private final List<Predictor<?>> predictors;

    public PredictorService(ResultRepository resultRepository,
                            List<Predictor<?>> predictors) {
        this.resultRepository = resultRepository;
        this.predictors = predictors;
    }

    public Map<String, Object> run() {

        List<Result> allResults = resultRepository.findAll();

        if (allResults.size() < 2) {
            throw new RuntimeException("NOT_ENOUGH_DATA");
        }

        allResults.sort(Comparator.comparing(Result::getDate));

        Map<String, Object> result = new HashMap<>();

        for (Predictor<?> p : predictors) {
            Object data = p.predict(allResults);
            result.put(p.getName(), data);
        }

        return result;
    }
}