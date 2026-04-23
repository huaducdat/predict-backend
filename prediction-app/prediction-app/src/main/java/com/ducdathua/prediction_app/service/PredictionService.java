package com.ducdathua.prediction_app.service;

import com.ducdathua.prediction_app.dto.ChainScoreDto;
import com.ducdathua.prediction_app.dto.NumberScoreDto;
import com.ducdathua.prediction_app.dto.PredictionResponseDto;
import com.ducdathua.prediction_app.model.PredictionResult;
import com.ducdathua.prediction_app.model.Result;
import com.ducdathua.prediction_app.repository.PredictionResultRepository;
import com.ducdathua.prediction_app.repository.ResultRepository;
import com.ducdathua.prediction_app.service.util.PredictionMath;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PredictionService {

    private final ResultRepository resultRepository;
    private final List<PredictionChain> predictionChains;
    private final PredictionResultService predictionResultService;

    public PredictionService(ResultRepository resultRepository,
                             List<PredictionChain> predictionChains,
                             PredictionResultService predictionResultService
                           ) {
        this.resultRepository = resultRepository;
        this.predictionChains = predictionChains;
        this.predictionResultService = predictionResultService;

    }

    public PredictionResponseDto predict() {
        List<Result> allResults = resultRepository.findAll();

        Map<Integer, Double> finalMap = PredictionMath.initZeroMap();
        List<ChainScoreDto> chainDtos = new ArrayList<>();

        for (PredictionChain chain : predictionChains) {
            Map<Integer, Double> chainScores = chain.calculateScores(allResults);
            finalMap = PredictionMath.addWeighted(finalMap, chainScores, chain.getWeight());

            List<NumberScoreDto> topNumbers = PredictionMath.toSortedDtoList(chainScores)
                    .stream()
                    .limit(10)
                    .toList();

            chainDtos.add(new ChainScoreDto(
                    chain.getName(),
                    chain.getWeight(),
                    topNumbers
            ));
        }

        finalMap = PredictionMath.normalize(finalMap);
        List<NumberScoreDto> finalSorted = PredictionMath.toSortedDtoList(finalMap);

        List<NumberScoreDto> top3 = finalSorted.stream().limit(3).toList();
        List<NumberScoreDto> top15 = finalSorted.stream().limit(15).toList();

        PredictionResponseDto response = new PredictionResponseDto(
                top3,
                top15,
                finalSorted, // 👈 thêm cái này
                chainDtos
        );

    // lưu DB
        predictionResultService.saveOrUpdateToday(top3, top15, finalSorted);

        return response;

    }


}