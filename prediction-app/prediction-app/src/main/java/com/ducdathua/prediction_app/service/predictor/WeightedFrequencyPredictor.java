package com.ducdathua.prediction_app.service.predictor;


import com.ducdathua.prediction_app.model.Result;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class WeightedFrequencyPredictor {
    private Map<Integer, Map<Integer, Integer>> buildNextNumberFrequency(List<Result> allResults) {

        Map<Integer, Map<Integer, Integer>> memory = new HashMap<>();

        // init 00-99
        for (int i = 0; i < 100; i++) {
            memory.put(i, new HashMap<>());
        }

        // cần ít nhất 2 ngày mới soi được "sau nó là gì"
        for (int i = 0; i < allResults.size() - 1; i++) {

            Result currentDay = allResults.get(i);
            Result nextDay = allResults.get(i + 1);

            // số xuất hiện hôm nay
            Set<Integer> currentNumbers = new HashSet<>(currentDay.getNumbers());
            currentNumbers.add(currentDay.getSingleNumber());

            // số xuất hiện ngày mai
            Set<Integer> nextNumbers = new HashSet<>(nextDay.getNumbers());
            nextNumbers.add(nextDay.getSingleNumber());

            // mỗi số hôm nay sẽ ghi nhớ các số hay về ngày mai
            for (Integer source : currentNumbers) {
                Map<Integer, Integer> freqMap = memory.get(source);

                for (Integer target : nextNumbers) {
                    freqMap.put(target, freqMap.getOrDefault(target, 0) + 1);
                }
            }
        }

        return memory;
    }

    public Map<Integer, Map<Integer, Integer>> buildFullMemory(List<Result> allResults) {
        return buildNextNumberFrequency(allResults);
    }

    public Map<Integer, List<Integer>> buildTop3FromMemory(
            Map<Integer, Map<Integer, Integer>> memory) {

        Map<Integer, List<Integer>> result = new HashMap<>();

        for (int source = 0; source < 100; source++) {

            Map<Integer, Integer> freqMap = memory.get(source);

            if (freqMap == null || freqMap.isEmpty()) {
                result.put(source, List.of());
                continue;
            }

            List<Integer> top3 = freqMap.entrySet()
                    .stream()
                    .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                    .limit(3)
                    .map(Map.Entry::getKey)
                    .toList();

            result.put(source, top3);
        }

        return result;
    }

    public Map<Integer, List<Integer>> buildTop3(List<Result> allResults) {

        Map<Integer, Map<Integer, Integer>> memory =
                buildNextNumberFrequency(allResults);

        return buildTop3FromMemory(memory);
    }

    public Map<Integer, List<Map.Entry<Integer, Integer>>> buildTop3WithScore(
            Map<Integer, Map<Integer, Integer>> memory) {

        Map<Integer, List<Map.Entry<Integer, Integer>>> result = new HashMap<>();

        for (int source = 0; source < 100; source++) {

            Map<Integer, Integer> freqMap = memory.get(source);

            List<Map.Entry<Integer, Integer>> top3 = freqMap.entrySet()
                    .stream()
                    .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                    .limit(3)
                    .toList();

            result.put(source, top3);
        }

        return result;
    }

    public Map<Integer, List<Map.Entry<Integer, Integer>>> buildTop3WithScore(List<Result> allResults) {

        Map<Integer, Map<Integer, Integer>> memory =
                buildNextNumberFrequency(allResults);

        return buildTop3WithScore(memory);
    }
}