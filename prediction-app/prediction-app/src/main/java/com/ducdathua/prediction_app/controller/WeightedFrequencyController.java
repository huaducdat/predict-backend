package com.ducdathua.prediction_app.controller;

import com.ducdathua.prediction_app.dto.WeightedFrequencyNumberFullDto;
import com.ducdathua.prediction_app.service.chainService.WeightedFrequencyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wf/predictor")
@CrossOrigin
public class WeightedFrequencyController {

    private final WeightedFrequencyService service;

    public WeightedFrequencyController(WeightedFrequencyService service) {
        this.service = service;
    }

    @GetMapping("/full")
    public List<WeightedFrequencyNumberFullDto> getFull() {
        return service.getFullMemory();
    }

    // 🔥 2. TOP 3 của tất cả 100 số
    @GetMapping("/top3")
    public Map<Integer, List<Integer>> getTop3() {
        return service.getTop3FromFullDto();
    }

    // 🔥 3. TOP 3 của 1 số cụ thể (debug nhanh)
    @GetMapping("/top3/{number}")
    public List<Integer> getTop3ByNumber(@PathVariable int number) {
        return service.getTop3FromFullDto().get(number);
    }

    @GetMapping("/top3-score")
    public Map<Integer, List<Map.Entry<Integer, Integer>>> getTop3Score() {
        return service.getTop3WithScore();
    }
}