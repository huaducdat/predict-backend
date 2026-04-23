package com.ducdathua.prediction_app.dto;

import java.util.List;

public class ChainScoreDto {
    private String chainName;
    private double chainWeight;
    private List<NumberScoreDto> topNumbers;

    public ChainScoreDto() {
    }

    public ChainScoreDto(String chainName, double chainWeight, List<NumberScoreDto> topNumbers) {
        this.chainName = chainName;
        this.chainWeight = chainWeight;
        this.topNumbers = topNumbers;
    }

    public String getChainName() {
        return chainName;
    }

    public void setChainName(String chainName) {
        this.chainName = chainName;
    }

    public double getChainWeight() {
        return chainWeight;
    }

    public void setChainWeight(double chainWeight) {
        this.chainWeight = chainWeight;
    }

    public List<NumberScoreDto> getTopNumbers() {
        return topNumbers;
    }

    public void setTopNumbers(List<NumberScoreDto> topNumbers) {
        this.topNumbers = topNumbers;
    }
}
