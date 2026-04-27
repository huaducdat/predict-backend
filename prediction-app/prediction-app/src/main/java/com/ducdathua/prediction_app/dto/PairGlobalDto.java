package com.ducdathua.prediction_app.dto;

import java.util.List;

public class PairGlobalDto {

    private int pairKey;
    private String pair;
    private List<NumberScoreDto> targets;

    public PairGlobalDto(int pairKey, String pair, List<NumberScoreDto> targets) {
        this.pairKey = pairKey;
        this.pair = pair;
        this.targets = targets;
    }

    public int getPairKey() {
        return pairKey;
    }

    public String getPair() {
        return pair;
    }

    public List<NumberScoreDto> getTargets() {
        return targets;
    }
}