package com.ducdathua.prediction_app.service.predictor;

import com.ducdathua.prediction_app.dto.NumberScoreDto;
import com.ducdathua.prediction_app.model.Result;

import java.util.List;
import java.util.Map;

public interface Predictor<T> {
    String getName();
    T predict(List<Result> allResults);
}