package com.ducdathua.prediction_app.service;

import com.ducdathua.prediction_app.dto.NumberScoreDto;
import com.ducdathua.prediction_app.dto.PredictionResponseDto;
import com.ducdathua.prediction_app.model.PredictionNumber;
import com.ducdathua.prediction_app.model.PredictionResult;
import com.ducdathua.prediction_app.repository.PredictionResultRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PredictionResultService {

    private final PredictionResultRepository repository;

    public PredictionResultService(PredictionResultRepository repository) {
        this.repository = repository;
    }

    public void saveOrUpdateToday(List<NumberScoreDto> top3,
                                  List<NumberScoreDto> top15,
                                  List<NumberScoreDto> full) {

        LocalDate today = LocalDate.now();

        List<PredictionNumber> fullList = full.stream()
                .map(n -> new PredictionNumber(
                        n.getNumber(),
                        n.getScore(),
                        n.getPercentage()
                ))
                .collect(Collectors.toList());

        List<Integer> top3Numbers = top3.stream()
                .map(NumberScoreDto::getNumber)
                .collect(Collectors.toList());

        List<Integer> top15Numbers = top15.stream()
                .map(NumberScoreDto::getNumber)
                .collect(Collectors.toList());

        PredictionResult entity = repository.findByDate(today)
                .orElse(new PredictionResult(today, top3Numbers, top15Numbers, fullList));

        entity.setTop3(top3Numbers);
        entity.setTop15(top15Numbers);
        entity.setFull(fullList);

        repository.save(entity);
    }

    public PredictionResponseDto getTodayPrediction() {
        PredictionResult entity = repository.findByDate(LocalDate.now()).orElse(null);

        if (entity == null) {
            return null;
        }

        List<NumberScoreDto> fullDto = entity.getFull().stream()
                .map(n -> new NumberScoreDto(
                        n.getNumber(),
                        n.getScore(),
                        n.getPercentage()
                ))
                .collect(Collectors.toList());

        List<NumberScoreDto> top3Dto = entity.getTop3().stream()
                .map(number -> {
                    PredictionNumber matched = entity.getFull().stream()
                            .filter(n -> n.getNumber() == number)
                            .findFirst()
                            .orElse(new PredictionNumber(number, 0, 0));
                    return new NumberScoreDto(
                            matched.getNumber(),
                            matched.getScore(),
                            matched.getPercentage()
                    );
                })
                .collect(Collectors.toList());

        List<NumberScoreDto> top15Dto = entity.getTop15().stream()
                .map(number -> {
                    PredictionNumber matched = entity.getFull().stream()
                            .filter(n -> n.getNumber() == number)
                            .findFirst()
                            .orElse(new PredictionNumber(number, 0, 0));
                    return new NumberScoreDto(
                            matched.getNumber(),
                            matched.getScore(),
                            matched.getPercentage()
                    );
                })
                .collect(Collectors.toList());

        return new PredictionResponseDto(top3Dto, top15Dto, fullDto, List.of());
    }
}