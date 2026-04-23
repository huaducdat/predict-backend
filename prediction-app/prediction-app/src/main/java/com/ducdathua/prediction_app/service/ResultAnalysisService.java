package com.ducdathua.prediction_app.service;

import com.ducdathua.prediction_app.dto.NumberStreakResponse;
import com.ducdathua.prediction_app.model.LatestDayStreak;
import com.ducdathua.prediction_app.model.Result;
import com.ducdathua.prediction_app.model.StreakInfo;
import com.ducdathua.prediction_app.repository.LatestDayStreakRepository;
import com.ducdathua.prediction_app.repository.ResultRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ResultAnalysisService {
    private final LatestDayStreakRepository latestRepo;
    public ResultAnalysisService(LatestDayStreakRepository latestRepo) {
        this.latestRepo = latestRepo;
    }
    public List<NumberStreakResponse> getLatestDayStreaks() {

        return latestRepo.findAll()
                .stream()
                .filter(LatestDayStreak::isAppearedToday) // 👈 KEY
                .sorted(Comparator.comparing(LatestDayStreak::getNumber))
                .map(s -> new NumberStreakResponse(
                        s.getNumber(),
                        s.getCurrentStreak(),
                        s.getMaxStreak()
                ))
                .toList();
    }
}
