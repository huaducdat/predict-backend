package com.ducdathua.prediction_app.controller;

import com.ducdathua.prediction_app.dto.PredictionResponseDto;
import com.ducdathua.prediction_app.model.PredictionResult;
import com.ducdathua.prediction_app.service.PredictionResultService;
import com.ducdathua.prediction_app.service.PredictionService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/predictions")
@CrossOrigin(origins = "http://localhost:5173")
public class PredictionController {

    private final PredictionService predictionService;
    private PredictionResultService predictionResultService;
    public PredictionController(PredictionService predictionService, PredictionResultService predictionResultService) {
        this.predictionService = predictionService;
        this.predictionResultService = predictionResultService;
    }

    @GetMapping
    public PredictionResponseDto predict() {
        return predictionService.predict();
    }

    @GetMapping("/today")
    public PredictionResponseDto getTodayPrediction() {
        return predictionResultService.getTodayPrediction();
    }
}