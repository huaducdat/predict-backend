package com.ducdathua.prediction_app.service;

import com.ducdathua.prediction_app.model.NumberStreakResponse;
import com.ducdathua.prediction_app.model.Result;
import com.ducdathua.prediction_app.model.StreakInfo;
import com.ducdathua.prediction_app.repository.ResultRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ResultAnalysisService {
    private final ResultRepository repo;

    public ResultAnalysisService(ResultRepository repo) {
        this.repo = repo;
    }

    public List<NumberStreakResponse> getLatestDayStreaks() {
        List<Result> results = repo.findAll()
                .stream()
                .sorted(Comparator.comparing(Result::getDate))
                .toList();

        if(results.isEmpty()) {
            return  List.of();
        }

        Map<Integer, StreakInfo> streakMap = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            streakMap.put(i, new StreakInfo());
        }

        LocalDate prevDate = null;
        Result latestResult = null;

        for (Result r : results) {
            LocalDate currentDate = r.getDate();
            Set<Integer> todayNumbers = new HashSet<>(r.getNumbers());
            boolean isBrokenByMissingDate = prevDate != null && !currentDate.equals(prevDate.plusDays(1));

            if(isBrokenByMissingDate) {
                for (int i = 0; i < 100; i++) {
                    streakMap.get(i).reset();
                }
            }
            for (int i = 0; i < 100; i++) {
                if (todayNumbers.contains(i)) {
                    streakMap.get(i).increase();
                } else {
                    streakMap.get(i).reset();
                }
            }

            prevDate = currentDate;
            latestResult = r;
        }

        Set<Integer> latestNumbers = new HashSet<>(latestResult.getNumbers());

        return latestNumbers.stream()
                .sorted()
                .map(number -> {
                    StreakInfo info = streakMap.get(number);
                    return new NumberStreakResponse(
                            number,
                            info.getCurrent(),
                            info.getMax()
                    );
                })
                .toList();
    }
}
