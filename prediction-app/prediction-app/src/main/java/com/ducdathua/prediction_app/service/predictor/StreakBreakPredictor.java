package com.ducdathua.prediction_app.service.predictor;

import com.ducdathua.prediction_app.dto.NumberScoreDto;
import com.ducdathua.prediction_app.model.LatestDayStreak;
import com.ducdathua.prediction_app.model.Result;
import com.ducdathua.prediction_app.repository.LatestDayStreakRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StreakBreakPredictor implements Predictor {

    private final LatestDayStreakRepository streakRepo;

    private static final double K = 4.0;

    public StreakBreakPredictor(LatestDayStreakRepository streakRepo) {
        this.streakRepo = streakRepo;
    }

    @Override
    public String getName() {
        return "STREAK_BREAK";
    }

    @Override
    public Map<Integer, List<NumberScoreDto>> predict(List<Result> ignore) {

        List<LatestDayStreak> streaks = streakRepo.findAll();

        Map<Integer, Double> scoreMap = new HashMap<>();

        for (int i = 0; i < 100; i++) {
            scoreMap.put(i, 1.0);
        }

        LocalDate today = LocalDate.now();

        for (LatestDayStreak s : streaks) {

            int number = s.getNumber();

            if (!s.isAppearedToday()) {
                continue;
            }

            int current = s.getCurrentStreak();
            int max = Math.max(1, s.getMaxStreak());

            double ratio = (double) current / max;

            double baseScore = 1.0 / (1 + ratio * K);

            long daysDiff = ChronoUnit.DAYS.between(s.getLastUpdated(), today);

            double timeWeight = getTimeWeight(daysDiff);

            double finalScore = baseScore * timeWeight;

            // nếu quá cũ → bỏ phạt luôn
            if (timeWeight == 0) {
                finalScore = 1.0;
            }

            scoreMap.put(number, finalScore);
        }

        List<NumberScoreDto> sorted = scoreMap.entrySet()
                .stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .map(e -> new NumberScoreDto(e.getKey(), e.getValue()))
                .toList();

        Map<Integer, List<NumberScoreDto>> result = new HashMap<>();
        result.put(-1, sorted);

        return result;
    }

    private double getTimeWeight(long daysDiff) {
        if (daysDiff <= 30) return 1.0;
        if (daysDiff <= 90) return 0.6;
        if (daysDiff <= 180) return 0.3;
        return 0;
    }
}