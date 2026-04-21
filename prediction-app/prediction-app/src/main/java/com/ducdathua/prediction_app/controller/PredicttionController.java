package com.ducdathua.prediction_app.controller;

import com.ducdathua.prediction_app.dto.PredictionRequest;
import com.ducdathua.prediction_app.service.PredictionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/prediction")
public class PredicttionController {
    private final PredictionService service;
    public PredicttionController(PredictionService service) {
        this.service = service;
    }
    @PostMapping
    public String create(@RequestBody PredictionRequest req) {
        service.create(req);
        return "OK";
    }
}
