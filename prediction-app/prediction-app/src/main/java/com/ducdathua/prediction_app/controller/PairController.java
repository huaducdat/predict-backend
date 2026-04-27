package com.ducdathua.prediction_app.controller;

import com.ducdathua.prediction_app.dto.PairGlobalDto;
import com.ducdathua.prediction_app.service.PairGlobalService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/pair")

public class PairController {

    private final PairGlobalService pairGlobalService;

    public PairController(PairGlobalService pairGlobalService) {
        this.pairGlobalService = pairGlobalService;
    }

    @GetMapping("/global")
    public List<PairGlobalDto> getGlobal() {
        return pairGlobalService.getGlobal();
    }
}