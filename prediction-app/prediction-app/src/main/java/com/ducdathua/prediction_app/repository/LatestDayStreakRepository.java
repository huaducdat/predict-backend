package com.ducdathua.prediction_app.repository;

import com.ducdathua.prediction_app.model.LatestDayStreak;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LatestDayStreakRepository extends JpaRepository<LatestDayStreak, Integer> {
}
