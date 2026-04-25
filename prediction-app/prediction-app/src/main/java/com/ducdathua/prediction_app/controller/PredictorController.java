package com.ducdathua.prediction_app.controller;

import com.ducdathua.prediction_app.dto.NumberScoreDto;
import com.ducdathua.prediction_app.service.PredictorService;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/predict")
public class PredictorController {

    private final PredictorService service;

    public PredictorController(PredictorService service) {
        this.service = service;
    }

    @GetMapping
    public Map<String, Map<Integer, List<NumberScoreDto>>> getTop3Count() {
        return service.run();
    }
}