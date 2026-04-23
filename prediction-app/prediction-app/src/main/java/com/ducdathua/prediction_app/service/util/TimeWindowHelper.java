package com.ducdathua.prediction_app.service.util;

import com.ducdathua.prediction_app.model.Result;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class TimeWindowHelper {
    private TimeWindowHelper() {
    }

    public static List<Result> sortByDateAsc(List<Result> results) {
        return results.stream()
                .sorted(Comparator.comparing(Result::getDate))
                .toList();
    }

    public static List<Result> filterLastDays(List<Result> results, int days) {
        if (results.isEmpty()) return List.of();

        List<Result> sorted = sortByDateAsc(results);
        LocalDate latest = sorted.get(sorted.size() - 1).getDate();
        LocalDate minDate = latest.minusDays(days - 1L);

        return sorted.stream()
                .filter(r -> !r.getDate().isBefore(minDate))
                .toList();
    }
}