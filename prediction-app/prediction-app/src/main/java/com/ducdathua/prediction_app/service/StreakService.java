package com.ducdathua.prediction_app.service;

import com.ducdathua.prediction_app.model.NumberStreakResponse;
import com.ducdathua.prediction_app.model.Result;
import com.ducdathua.prediction_app.repository.ResultRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StreakService {
    private final ResultRepository resultRepo;
    private final NumberStreakResponse numberStrPes;

    public StreakService(ResultRepository resultRepo, NumberStreakResponse numberStrPes) {
        this.resultRepo = resultRepo;
        this.numberStrPes = numberStrPes;
    }

    @Transactional
    public void rebuilAllStreaks() {
        List<Result> results = resultRepo.findAll(); 
    }
}
