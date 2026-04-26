package com.ducdathua.prediction_app.service;

import com.ducdathua.prediction_app.dto.NumberScoreDto;
import com.ducdathua.prediction_app.model.Result;
import com.ducdathua.prediction_app.model.TimeWeightGlobal;
import com.ducdathua.prediction_app.repository.ResultRepository;
import com.ducdathua.prediction_app.repository.TimeWeightGlobalRepository;
import com.ducdathua.prediction_app.service.predictor.TimeWeightedScorePredictor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TimeWeightGlobalService {

    private final TimeWeightGlobalRepository repo;
    private final ObjectMapper objectMapper;

    public TimeWeightGlobalService(TimeWeightGlobalRepository repo,
                                   ObjectMapper objectMapper) {
        this.repo = repo;
        this.objectMapper = objectMapper;
    }

    // ===== SAVE =====
    public void save(LocalDate date,
                     Map<Integer, List<NumberScoreDto>> global) {

        try {
            String json = objectMapper.writeValueAsString(global);

            TimeWeightGlobal entity =
                    repo.findByDate(date)
                            .orElse(new TimeWeightGlobal());

            entity.setDate(date);
            entity.setData(json);

            repo.save(entity);

        } catch (Exception e) {
            throw new RuntimeException("Save TimeWeightGlobal failed", e);
        }
    }

    // ===== LOAD =====
    public Map<Integer, List<NumberScoreDto>> load(LocalDate date) {

        try {
            TimeWeightGlobal entity = repo.findByDate(date)
                    .orElseThrow(() -> new RuntimeException("Not found"));

            // 🔥 1. đọc JSON dạng String key
            Map<String, List<NumberScoreDto>> raw =
                    objectMapper.readValue(
                            entity.getData(),
                            new TypeReference<Map<String, List<NumberScoreDto>>>() {}
                    );

            // 🔥 2. convert sang Integer key
            Map<Integer, List<NumberScoreDto>> result = new HashMap<>();

            for (Map.Entry<String, List<NumberScoreDto>> e : raw.entrySet()) {
                result.put(Integer.parseInt(e.getKey()), e.getValue());
            }

            return result;

        } catch (Exception e) {
            e.printStackTrace(); // 👈 để debug nếu còn lỗi
            throw new RuntimeException("Load TimeWeightGlobal failed", e);
        }
    }
}