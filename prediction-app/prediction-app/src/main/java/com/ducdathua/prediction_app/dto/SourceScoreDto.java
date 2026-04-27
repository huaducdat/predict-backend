package com.ducdathua.prediction_app.dto;

public class SourceScoreDto {

    private String pair;
    private double score;

    public SourceScoreDto() {}

    public SourceScoreDto(String pair, double score) {
        this.pair = pair;
        this.score = score;
    }

    public String getPair() { return pair; }
    public double getScore() { return score; }
}