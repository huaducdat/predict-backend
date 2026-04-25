package com.ducdathua.prediction_app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class LatestDayStreak {
    @Id
    private int number;
    private int maxStreak;
    private int currentStreak;
    private boolean appearedToday;
    private LocalDate lastUpdated;

    public LatestDayStreak() {}

    public LatestDayStreak(int number, int currentStreak, int maxStreak, boolean appearedToday, LocalDate lastUpdated) {
        this.number = number;
        this.currentStreak = currentStreak;
        this.maxStreak = maxStreak;
        this.appearedToday = appearedToday;
        this.lastUpdated = lastUpdated;
    }

    public int getNumber() { return number; }
    public int getCurrentStreak() { return currentStreak; }
    public int getMaxStreak() { return maxStreak; }
    public boolean isAppearedToday() { return appearedToday; }
    public LocalDate getLastUpdated() {
        return lastUpdated;
    }
}
