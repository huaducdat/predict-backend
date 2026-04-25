package com.ducdathua.prediction_app.controller;

import com.ducdathua.prediction_app.dto.NumberScoreDto;
import com.ducdathua.prediction_app.model.Result;
import com.ducdathua.prediction_app.repository.ResultRepository;
import com.ducdathua.prediction_app.service.combine.PredictionCombineService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import com.ducdathua.prediction_app.dto.CombineResponseDto;



@RestController
@RequestMapping("/api/combine")
@CrossOrigin(origins = "http://localhost:5173")
public class CombineController {

    private final PredictionCombineService combineService;
    private final ResultRepository resultRepo;

    public CombineController(PredictionCombineService combineService,
                             ResultRepository resultRepo) {
        this.combineService = combineService;
        this.resultRepo = resultRepo;
    }

    @GetMapping
    public CombineResponseDto combine() {
        List<Result> all = resultRepo.findAll();
        return combineService.combine(all);
    }
}