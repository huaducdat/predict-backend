package com.ducdathua.prediction_app.service.predictor;

import com.ducdathua.prediction_app.dto.NumberScoreDto;
import com.ducdathua.prediction_app.model.LatestDayStreak;
import com.ducdathua.prediction_app.model.Result;
import com.ducdathua.prediction_app.repository.LatestDayStreakRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class StreakContinuePredictor implements Predictor<Map<Integer, List<NumberScoreDto>>> {

    private final LatestDayStreakRepository streakRepo;

    public StreakContinuePredictor(LatestDayStreakRepository streakRepo) {
        this.streakRepo = streakRepo;
    }

    @Override
    public String getName() {
        return "STREAK_CONTINUE";
    }

    @Override
    public Map<Integer, List<NumberScoreDto>> predict(List<Result> ignore) {

        List<LatestDayStreak> streaks = streakRepo.findAll();

        // 🔥 chỉ giữ số xuất hiện hôm nay
        List<LatestDayStreak> todayStreaks = streaks.stream()
                .filter(LatestDayStreak::isAppearedToday)
                .collect(Collectors.toList());

        Map<Integer, Double> scoreMap = new HashMap<>();

        LocalDate today = LocalDate.now();

        for (LatestDayStreak s : todayStreaks) {

            int number = s.getNumber();

            int current = Math.max(0, s.getCurrentStreak());
            int max = Math.max(1, s.getMaxStreak());

            // ===== progress =====
            double progress = Math.min(1.0, (double) current / max);

            // ===== còn dư địa =====
            double roomScore = 1.0 - progress;

            // tránh chết hẳn
            roomScore = Math.max(0.05, roomScore);

            // ===== time =====
            long daysDiff = ChronoUnit.DAYS.between(s.getLastUpdated(), today);
            double timeWeight = getTimeWeight(daysDiff);

            // ===== bonus nếu có streak =====
            double streakBonus = Math.min(1.0, current / 3.0);

            // ===== final =====
            double finalScore =
                    (roomScore * 0.75)
                            + (streakBonus * 0.25);

            finalScore *= timeWeight;

            scoreMap.put(number, round(finalScore));
        }

        // 🔥 sort giảm dần (score cao = nên chọn)
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
        if (daysDiff <= 1) return 1.0;
        if (daysDiff <= 7) return 0.8;
        if (daysDiff <= 30) return 0.5;
        return 0.2;
    }

    private double round(double value) {
        return Math.round(value * 10000.0) / 10000.0;
    }
}