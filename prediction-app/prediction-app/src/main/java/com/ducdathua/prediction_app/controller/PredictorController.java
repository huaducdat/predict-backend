package com.ducdathua.prediction_app.controller;

import com.ducdathua.prediction_app.dto.NumberScoreDto;
import com.ducdathua.prediction_app.model.PredictionSnapshot;
import com.ducdathua.prediction_app.repository.PredictionSnapshotRepository;
import com.ducdathua.prediction_app.service.PredictorService;

import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/predict")
@CrossOrigin(origins = "http://localhost:5173")
public class PredictorController {

    private final PredictorService service;
    private final PredictionSnapshotRepository repo;
    private final ObjectMapper objectMapper;

    public PredictorController(PredictorService service,
                               PredictionSnapshotRepository repo,
                               ObjectMapper objectMapper) {
        this.service = service;
        this.repo = repo;
        this.objectMapper = objectMapper;
    }

    // 🔥 1. RUN + SAVE
    @PostMapping("/run")
    public Map<String, Object> runPredict() {
        return service.run();
    }

    // 🔥 2. LOAD LATEST (KHÔNG RUN)
    @GetMapping("/latest")
    public Map<String, Object> getLatest() throws Exception {

        PredictionSnapshot snapshot = repo
                .findTopByOrderByDateDesc()
                .orElseThrow(() -> new RuntimeException("NO_SNAPSHOT"));

        if (snapshot == null) return Map.of();

        Map<String, Object> data = objectMapper.readValue(
                snapshot.getJsonData(),
                Map.class
        );

        return Map.of(
                "date", snapshot.getDate(),
                "createdAt", snapshot.getCreatedAt(),
                "data", data
        );
    }
}