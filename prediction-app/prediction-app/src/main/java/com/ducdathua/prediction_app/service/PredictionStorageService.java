package com.ducdathua.prediction_app.service;

import com.ducdathua.prediction_app.model.PredictionSnapshot;
import com.ducdathua.prediction_app.repository.PredictionSnapshotRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class PredictionStorageService {

    private final PredictionSnapshotRepository repo;
    private final ObjectMapper objectMapper;

    public PredictionStorageService(
            PredictionSnapshotRepository repo,
            ObjectMapper objectMapper) {
        this.repo = repo;
        this.objectMapper = objectMapper;
    }

    public void save(LocalDate date, Map<String, Object> data) {

        try {
            String json = objectMapper.writeValueAsString(data);

            PredictionSnapshot snapshot =
                    repo.findByDate(date)
                            .orElse(new PredictionSnapshot());

            snapshot.setDate(date);
            snapshot.setJsonData(json);
            snapshot.setCreatedAt(LocalDateTime.now());
            System.out.println("🔥 SAVE CALLED: " + LocalDateTime.now());
            repo.save(snapshot);

        } catch (Exception e) {
            throw new RuntimeException("SAVE_JSON_FAILED", e);
        }
    }
}