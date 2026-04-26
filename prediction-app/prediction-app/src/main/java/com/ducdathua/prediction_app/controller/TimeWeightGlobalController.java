package com.ducdathua.prediction_app.controller;

import com.ducdathua.prediction_app.dto.NumberScoreDto;
import com.ducdathua.prediction_app.service.TimeWeightGlobalService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")

@RestController
@RequestMapping("/api/time-weight-global")

public class TimeWeightGlobalController {

    private final TimeWeightGlobalService service;

    public TimeWeightGlobalController(TimeWeightGlobalService service) {
        this.service = service;
    }

    @GetMapping("/{date}")
    public Map<Integer, List<NumberScoreDto>> get(@PathVariable String date) {
        return service.load(LocalDate.parse(date));
    }
}