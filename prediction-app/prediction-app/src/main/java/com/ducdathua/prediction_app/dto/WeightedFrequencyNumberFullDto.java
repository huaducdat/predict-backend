package com.ducdathua.prediction_app.dto;

import java.util.List;
import java.util.Map;

public class WeightedFrequencyNumberFullDto {

    private int source;
    private Map<Integer, Integer> nextMap;

    public WeightedFrequencyNumberFullDto(int source, Map<Integer, Integer> nextMap) {
        this.source = source;
        this.nextMap = nextMap;
    }

    public int getSource() {
        return source;
    }

    public Map<Integer, Integer> getNextMap() {
        return nextMap;
    }

    public List<Integer> extractTop3(WeightedFrequencyNumberFullDto dto) {

        return dto.getNextMap().entrySet()
                .stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .limit(3)
                .map(Map.Entry::getKey)
                .toList();
    }

    public List<Integer> extractTop3WithScore() {

        return nextMap.entrySet()
                .stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .limit(3)
                .map(Map.Entry::getKey)
                .toList();
    }
}