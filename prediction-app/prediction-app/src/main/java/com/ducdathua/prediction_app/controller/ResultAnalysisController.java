package com.ducdathua.prediction_app.controller;

import com.ducdathua.prediction_app.model.NumberStreakResponse;
import com.ducdathua.prediction_app.service.ResultAnalysisService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analysis")
@CrossOrigin(origins = "http://localhost:5173")
public class ResultAnalysisController {
    private final ResultAnalysisService service;

    public ResultAnalysisController(ResultAnalysisService resultAnalysisService) {
        this.service = resultAnalysisService;
    }

    @GetMapping("/latest-streaks")
    public List<NumberStreakResponse> getLatestStreaks() {
        return service.getLatestDayStreaks();
    }
}
