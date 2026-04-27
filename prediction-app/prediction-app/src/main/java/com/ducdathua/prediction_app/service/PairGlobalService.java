package com.ducdathua.prediction_app.service;

import com.ducdathua.prediction_app.dto.NumberScoreDto;
import com.ducdathua.prediction_app.dto.PairGlobalDto;
import com.ducdathua.prediction_app.model.PairGlobal;
import com.ducdathua.prediction_app.repository.PairGlobalRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class PairGlobalService {

    private final PairGlobalRepository repository;
    private final ObjectMapper objectMapper;

    public PairGlobalService(PairGlobalRepository repository,
                             ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public List<PairGlobalDto> getGlobal() {

        LocalDate today = LocalDate.now();

        PairGlobal entity = repository.findByDate(today)
                .orElseThrow(() -> new RuntimeException("No global data"));

        try {
            Map<Integer, Map<Integer, Double>> memory =
                    objectMapper.readValue(
                            entity.getData(),
                            new TypeReference<Map<Integer, Map<Integer, Double>>>() {}
                    );

            return memory.entrySet()
                    .stream()
                    .map(entry -> {

                        int pairKey = entry.getKey();
                        Map<Integer, Double> targetMap = entry.getValue();

                        List<NumberScoreDto> targets = targetMap.entrySet()
                                .stream()
                                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                                .limit(10)
                                .map(e -> new NumberScoreDto(e.getKey(), e.getValue()))
                                .toList();

                        return new PairGlobalDto(
                                pairKey,
                                decodePair(pairKey),
                                targets
                        );
                    })
                    .sorted((a, b) ->
                            Double.compare(
                                    sum(b.getTargets()),
                                    sum(a.getTargets())
                            )
                    )
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException("Parse JSON failed", e);
        }
    }

    private String decodePair(int pairKey) {
        int a = pairKey / 100;
        int b = pairKey % 100;
        return String.format("%02d-%02d", a, b);
    }

    private double sum(List<NumberScoreDto> list) {
        return list.stream()
                .mapToDouble(NumberScoreDto::getScore)
                .sum();
    }
}