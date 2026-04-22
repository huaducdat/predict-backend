package com.ducdathua.prediction_app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class NumberStreak {
    @Id
    private int number;
    private int currenStreak;
    private int maxStreak;

    private LocalDate lastUpdated;

    public NumberStreak() {

    }

    public NumberStreak(int number, int currenStreak, int maxStreak, LocalDate lastUpdated) {
        this.number = number;
        this.currenStreak = currenStreak;
        this.maxStreak = maxStreak;
        this.lastUpdated = lastUpdated;
    }
    public int getNumber() {return number;}
    public int getCurrenStreak() {return currenStreak;}
    public int getMaxStreak() {return maxStreak;}
    public LocalDate getLastUpdated() {return lastUpdated;}

    public void setCurrenStreak(int currenStreak) {this.currenStreak = currenStreak;}
    public void setMaxStreak(int maxStreak) {this.maxStreak = maxStreak;}
}
