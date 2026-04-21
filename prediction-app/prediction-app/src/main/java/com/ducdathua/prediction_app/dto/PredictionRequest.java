package com.ducdathua.prediction_app.dto;

import java.time.LocalDate;
import java.util.List;

public class PredictionRequest {
    private LocalDate date;
    private int singleNumber;
    private List<Integer> numbers;

    public LocalDate getDate() {
        return date;
    }

    public int getSingleNumber() {
        return singleNumber;
    }

    public List<Integer> getNumbers()
    {
        return this.numbers;
    }
}
