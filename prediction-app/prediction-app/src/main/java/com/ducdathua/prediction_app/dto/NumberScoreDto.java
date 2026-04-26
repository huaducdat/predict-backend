package com.ducdathua.prediction_app.dto;

public class NumberScoreDto {

    private int number;
    private double score; // 🔥 đổi sang double

    public NumberScoreDto() {}

    public NumberScoreDto(int number, double score) {
        this.number = number;
        this.score = score;
    }

    public int getNumber() { return number; }
    public double getScore() { return score; }
}