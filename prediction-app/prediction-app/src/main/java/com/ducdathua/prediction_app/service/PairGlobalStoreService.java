package com.ducdathua.prediction_app.service;

import com.ducdathua.prediction_app.model.PairGlobal;
import com.ducdathua.prediction_app.repository.PairGlobalRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;

@Service
public class PairGlobalStoreService {

    private final PairGlobalRepository repository;
    private final ObjectMapper objectMapper;

    public PairGlobalStoreService(PairGlobalRepository repository,
                                  ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void saveGlobal(Map<Integer, Map<Integer, Double>> memory) {

        try {
            String json = objectMapper.writeValueAsString(memory);

            LocalDate today = LocalDate.now();

            // 🔥 ghi đè
            repository.deleteByDate(today);

            PairGlobal entity = new PairGlobal(today, json);

            repository.save(entity);

        } catch (Exception e) {
            throw new RuntimeException("Save global failed", e);
        }
    }
}