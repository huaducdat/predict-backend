package com.ducdathua.prediction_app.dto;

import java.util.List;

public class CombineResponseDto {

    private List<NumberScoreDto> top3;
    private List<NumberScoreDto> top9;
    private List<NumberScoreDto> full;

    public CombineResponseDto(List<NumberScoreDto> full) {
        this.full = full;
        this.top3 = full.stream().limit(3).toList();
        this.top9 = full.stream().limit(9).toList();
    }

    public List<NumberScoreDto> getTop3() {
        return top3;
    }

    public List<NumberScoreDto> getTop9() {
        return top9;
    }

    public List<NumberScoreDto> getFull() {
        return full;
    }
}