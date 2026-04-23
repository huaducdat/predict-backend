package com.ducdathua.prediction_app.controller;

import com.ducdathua.prediction_app.model.LatestDayStreak;
import com.ducdathua.prediction_app.repository.LatestDayStreakRepository;
import com.ducdathua.prediction_app.service.StreakService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/streaks")
@CrossOrigin(origins = "http://localhost:5173")
public class StreakController {
    private final StreakService streakService;
    private final LatestDayStreakRepository latestRepo;

    public StreakController(StreakService service, LatestDayStreakRepository repo) {
        this.streakService = service;
        this.latestRepo = repo;
    }


    @PostMapping("/rebuild")
    public String rebuild() {
        streakService.rebuildLatestDayStreaks();
        return "REBUILT";
    }

    @GetMapping("/all")
    public List<LatestDayStreak> getAll() {
        return latestRepo.findAll();
    }
}
