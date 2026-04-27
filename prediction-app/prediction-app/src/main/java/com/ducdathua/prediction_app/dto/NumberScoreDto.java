package com.ducdathua.prediction_app.dto;

import java.util.List;

public class NumberScoreDto {

    private int number;
    private double score;
    private List<SourceScoreDto> sources; // 🔥 thêm dòng này

    public NumberScoreDto() {}

    // 🔥 constructor cũ (GIỮ LẠI)
    public NumberScoreDto(int number, double score) {
        this.number = number;
        this.score = score;
    }

    // 🔥 constructor mới
    public NumberScoreDto(int number, double score, List<SourceScoreDto> sources) {
        this.number = number;
        this.score = score;
        this.sources = sources;
    }

    public int getNumber() { return number; }
    public double getScore() { return score; }
    public List<SourceScoreDto> getSources() { return sources; }
}