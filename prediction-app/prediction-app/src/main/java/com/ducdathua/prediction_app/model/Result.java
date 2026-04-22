package com.ducdathua.prediction_app.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Result {
    @Id
    @GeneratedValue
    private Long id;
    private LocalDate date;
    private int singleNumber;
    @ElementCollection
    private List<Integer> numbers;

    public Result() {

    }

    public Result(LocalDate date, int singleNumber, List<Integer> numbers) {
        this.date = date;
        this.singleNumber = singleNumber;
        this.numbers = numbers;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getSingleNumber() {
        return singleNumber;
    }

    public List<Integer> getNumbers() {
        return numbers;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setSingleNumber(int singleNumber) {
        this.singleNumber = singleNumber;
    }

    public void setNumbers(List<Integer> numbers) {
        this.numbers = numbers;
    }
}
