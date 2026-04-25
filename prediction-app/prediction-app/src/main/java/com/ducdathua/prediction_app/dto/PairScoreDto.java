package com.ducdathua.prediction_app.dto;

import java.util.List;

public class PairScoreDto {
    private int pairKey;
    private double score;
    private List<NumberScoreDto> numbers;

    public PairScoreDto(int pairKey, double score, List<NumberScoreDto> numbers) {
        this.pairKey = pairKey;
        this.score = score;
        this.numbers = numbers;
    }

    public int getPairKey() {
        return pairKey;
    }

    public double getScore() {
        return score;
    }

    public List<NumberScoreDto> getNumbers() {
        return numbers;
    }
}