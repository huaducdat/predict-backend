package com.ducdathua.prediction_app.service.chain;

import com.ducdathua.prediction_app.model.Result;
import com.ducdathua.prediction_app.service.PredictionChain;
import com.ducdathua.prediction_app.service.util.PredictionMath;
import com.ducdathua.prediction_app.service.util.TimeWindowHelper;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PatternSimilarityChain implements PredictionChain {

    @Override
    public String getName() {
        return "PatternSimilarity";
    }

    @Override
    public double getWeight() {
        return 0.15;
    }

    @Override
    public Map<Integer, Double> calculateScores(List<Result> results) {

        Map<Integer, Double> raw = PredictionMath.initZeroMap();
        if (results.size() < 3) return raw;

        List<Result> sorted = TimeWindowHelper.sortByDateAsc(results);

        Set<Integer> today = new HashSet<>(
                sorted.get(sorted.size() - 1).getNumbers()
        );

        List<SimilarityItem> sims = new ArrayList<>();

        for (int i = 0; i < sorted.size() - 1; i++) {
            Result r = sorted.get(i);
            double sim = similarity(today, r.getNumbers());
            sims.add(new SimilarityItem(i, sim));
        }

        sims.sort((a, b) -> Double.compare(b.similarity, a.similarity));

        int topK = Math.min(5, sims.size());

        for (int i = 0; i < topK; i++) {
            int idx = sims.get(i).index;
            Result next = sorted.get(idx + 1);

            raw.put(next.getSingleNumber(),
                    raw.get(next.getSingleNumber()) + 1);
        }

        return PredictionMath.normalize(raw);
    }

    private double similarity(Set<Integer> a, List<Integer> b) {
        int count = 0;
        for (Integer n : b) {
            if (a.contains(n)) count++;
        }
        return (double) count / 27.0;
    }

    private static class SimilarityItem {
        int index;
        double similarity;

        public SimilarityItem(int index, double similarity) {
            this.index = index;
            this.similarity = similarity;
        }
    }
}