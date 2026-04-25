package com.ducdathua.prediction_app.service;

import com.ducdathua.prediction_app.dto.NumberScoreDto;
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
    private final List<Predictor> predictors;

    public PredictorService(ResultRepository resultRepository,
                            List<Predictor> predictors) {
        this.resultRepository = resultRepository;
        this.predictors = predictors;
    }

    public Map<String, Map<Integer, List<NumberScoreDto>>> run() {

        List<Result> allResults = resultRepository.findAll();

        // 🔥 Defensive
        if (allResults == null || allResults.size() < 2) {
            throw new RuntimeException("NOT_ENOUGH_DATA");
        }

        // 🔥 Sort theo ngày
        allResults.sort(Comparator.comparing(Result::getDate));

        Map<String, Map<Integer, List<NumberScoreDto>>> result = new HashMap<>();

        for (Predictor p : predictors) {
            result.put(p.getName(), p.predict(allResults));
        }

        return result;
    }
}