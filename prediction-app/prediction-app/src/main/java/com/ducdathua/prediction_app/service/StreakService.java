package com.ducdathua.prediction_app.service;

import com.ducdathua.prediction_app.model.LatestDayStreak;
import com.ducdathua.prediction_app.model.Result;
import com.ducdathua.prediction_app.repository.LatestDayStreakRepository;
import com.ducdathua.prediction_app.repository.ResultRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;



@Service
public class StreakService {
    private final ResultRepository resultRepo;
    private final LatestDayStreakRepository latestRepo;



    public StreakService(ResultRepository resultRepo, LatestDayStreakRepository latestRepo) {
        this.resultRepo = resultRepo;
        this.latestRepo = latestRepo;
    }

    @Transactional
    public void rebuildLatestDayStreaks() {

        List<Result> results = resultRepo.findAll();
        results.sort(Comparator.comparing(Result::getDate));

        if (results.isEmpty()) {
            latestRepo.deleteAll();
            return;
        }

        Map<Integer, Integer> currentMap = new HashMap<>();
        Map<Integer, Integer> maxMap = new HashMap<>();

        for (int i = 0; i < 100; i++) {
            currentMap.put(i, 0);
            maxMap.put(i, 0);
        }

        Result latest = null;
        LocalDate prevDate = null;

        for (Result r : results) {
            LocalDate currentDate = r.getDate();

            // Nếu bị đứt ngày thì reset toàn bộ current streak
            if (prevDate != null && !currentDate.equals(prevDate.plusDays(1))) {
                for (int i = 0; i < 100; i++) {
                    currentMap.put(i, 0);
                }
            }

            Set<Integer> todayNumbers = new HashSet<>(r.getNumbers());

            for (int i = 0; i < 100; i++) {
                if (todayNumbers.contains(i)) {
                    int current = currentMap.get(i) + 1;
                    currentMap.put(i, current);
                    maxMap.put(i, Math.max(maxMap.get(i), current));
                } else {
                    currentMap.put(i, 0);
                }
            }

            prevDate = currentDate;
            latest = r;
        }

        Set<Integer> latestNumbers = new HashSet<>(latest.getNumbers());
        List<LatestDayStreak> list = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            list.add(new LatestDayStreak(
                    i,
                    currentMap.get(i),
                    maxMap.get(i),
                    latestNumbers.contains(i),
                    LocalDate.now()
            ));
        }

        latestRepo.deleteAllInBatch(); // 🔥 QUAN TRỌNG

        latestRepo.saveAll(list);
    }
}