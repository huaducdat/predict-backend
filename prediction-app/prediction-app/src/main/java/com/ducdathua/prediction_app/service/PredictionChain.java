package com.ducdathua.prediction_app.service;

import com.ducdathua.prediction_app.model.Result;

import java.util.List;
import java.util.Map;

public interface PredictionChain {
    String getName();
    double getWeight();
    Map<Integer, Double> calculateScores(List<Result> allResults);
}