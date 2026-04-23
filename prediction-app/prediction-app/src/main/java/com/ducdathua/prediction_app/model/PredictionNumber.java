package com.ducdathua.prediction_app.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class PredictionNumber {

    private int number;
    private double score;
    private double percentage;

    public PredictionNumber() {}

    public PredictionNumber(int number, double score, double percentage) {
        this.number = number;
        this.score = score;
        this.percentage = percentage;
    }

    public int getNumber() {
        return number;
    }

    public double getScore() {
        return score;
    }

    public double getPercentage() {
        return percentage;
    }
}