package com.ducdathua.prediction_app.controller;

import com.ducdathua.prediction_app.dto.NumberStreakResponse;
import com.ducdathua.prediction_app.model.LatestDayStreak;
import com.ducdathua.prediction_app.service.ResultAnalysisService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

//    @PostMapping("/rebuild")
//    public String rebuild() {
//        service.rebuildLatestDayStreaks();
//        return "REBUILT";
//    }

}
