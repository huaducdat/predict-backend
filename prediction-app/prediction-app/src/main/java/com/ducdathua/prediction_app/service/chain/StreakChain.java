package com.ducdathua.prediction_app.service.chain;

import com.ducdathua.prediction_app.model.Result;
import com.ducdathua.prediction_app.service.PredictionChain;
import com.ducdathua.prediction_app.service.util.PredictionMath;
import com.ducdathua.prediction_app.service.util.TimeWindowHelper;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class StreakChain implements PredictionChain {

    @Override
    public String getName() {
        return "StreakChain";
    }

    @Override
    public double getWeight() {
        return 0.15;
    }

    @Override
    public Map<Integer, Double> calculateScores(List<Result> allResults) {
        List<Result> sorted = TimeWindowHelper.sortByDateAsc(allResults);
        Map<Integer, Double> raw = PredictionMath.initZeroMap();

        if (sorted.isEmpty()) {
            return raw;
        }

        for (int number = 0; number < 100; number++) {
            int streak = currentStreak(sorted, number);
            raw.put(number, (double) streak);
        }

        return PredictionMath.normalize(raw);
    }

    private int currentStreak(List<Result> sorted, int target) {
        int streak = 0;

        for (int i = sorted.size() - 1; i >= 0; i--) {
            Set<Integer> set = new HashSet<>(sorted.get(i).getNumbers());
            if (set.contains(target)) {
                streak++;
            } else {
                break;
            }
        }

        return streak;
    }
}