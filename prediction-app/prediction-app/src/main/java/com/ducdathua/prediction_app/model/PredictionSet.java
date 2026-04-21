package com.ducdathua.prediction_app.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.util.List;

@Entity
public class PredictionSet {
    @Id
    @GeneratedValue
    private Long id;
    private LocalDate date;
    @ElementCollection
    private List<Integer> numbers;
    private int singleNumber;


    public void setSingleNumber(int number) {
        this.singleNumber = number;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setNumbers(List<Integer> numbers) {
        this.numbers = numbers;
    }
}
