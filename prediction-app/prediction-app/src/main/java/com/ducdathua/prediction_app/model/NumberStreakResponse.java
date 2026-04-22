package com.ducdathua.prediction_app.model;

public class NumberStreakResponse {
    private int number;
    private int currentStreak;
    private int maxStreak;

    public NumberStreakResponse(int number, int currentStreak, int maxStreak) {
        this.number = number;
        this.currentStreak = currentStreak;
        this.maxStreak = maxStreak;
    }

    public int getNumber() {
        return number;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }
    public int getMaxStreak() {
        return maxStreak;
    }
}
