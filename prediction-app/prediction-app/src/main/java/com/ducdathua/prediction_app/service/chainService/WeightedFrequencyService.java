package com.ducdathua.prediction_app.service.chainService;

import com.ducdathua.prediction_app.dto.WeightedFrequencyNumberFullDto;
import com.ducdathua.prediction_app.model.Result;
import com.ducdathua.prediction_app.repository.ResultRepository;
import com.ducdathua.prediction_app.service.predictor.WeightedFrequencyPredictor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WeightedFrequencyService {

    private final ResultRepository resultRepository;
    private final WeightedFrequencyPredictor predictor;

    public WeightedFrequencyService(ResultRepository resultRepository,
                                    WeightedFrequencyPredictor predictor) {
        this.resultRepository = resultRepository;
        this.predictor = predictor;
    }

    public List<WeightedFrequencyNumberFullDto> getFullMemory() {

        List<Result> allResults = resultRepository.findAll();

        // ⚠️ bắt buộc sort
        allResults.sort((a, b) -> a.getDate().compareTo(b.getDate()));

        Map<Integer, Map<Integer, Integer>> memory =
                predictor.buildFullMemory(allResults);

        List<WeightedFrequencyNumberFullDto> result = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            result.add(new WeightedFrequencyNumberFullDto(i, memory.get(i)));
        }

        return result;
    }

    public Map<Integer, List<Integer>> getTop3FromFullDto() {

        List<Result> allResults = resultRepository.findAll();
        allResults.sort(Comparator.comparing(Result::getDate));

        Map<Integer, Map<Integer, Integer>> memory =
                predictor.buildFullMemory(allResults);

        Map<Integer, List<Integer>> result = new HashMap<>();

        for (int i = 0; i < 100; i++) {

            WeightedFrequencyNumberFullDto dto =
                    new WeightedFrequencyNumberFullDto(i, memory.get(i));

            List<Integer> top3 = dto.getNextMap().entrySet()
                    .stream()
                    .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                    .limit(3)
                    .map(Map.Entry::getKey)
                    .toList();

            result.put(i, top3);
        }

        return result;
    }

    public Map<Integer, List<Map.Entry<Integer, Integer>>> getTop3WithScore() {

        List<Result> allResults = resultRepository.findAll();
        allResults.sort(Comparator.comparing(Result::getDate));

        return predictor.buildTop3WithScore(allResults);
    }
}