package com.ducdathua.prediction_app.dto;

import java.util.List;

public class PredictionResponseDto {
    private List<NumberScoreDto> top3;
    private List<NumberScoreDto> top15;
    private List<ChainScoreDto> chains;
    private List<NumberScoreDto> full;

    public PredictionResponseDto() {
    }

    public PredictionResponseDto(List<NumberScoreDto> top3,
                                 List<NumberScoreDto> top15,
                                 List<NumberScoreDto> full,
                                 List<ChainScoreDto> chains) {
        this.top3 = top3;
        this.top15 = top15;
        this.full = full;
        this.chains = chains;
    }

    public List<NumberScoreDto> getTop3() {
        return top3;
    }

    public void setTop3(List<NumberScoreDto> top3) {
        this.top3 = top3;
    }

    public List<NumberScoreDto> getTop15() {
        return top15;
    }

    public void setTop15(List<NumberScoreDto> top15) {
        this.top15 = top15;
    }

    public List<ChainScoreDto> getChains() {
        return chains;
    }

    public void setChains(List<ChainScoreDto> chains) {
        this.chains = chains;
    }

    public List<NumberScoreDto> getFull() {return full;}
    public void setFull(List<NumberScoreDto> full) {this.full = full;}
}
