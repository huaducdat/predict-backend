package com.ducdathua.prediction_app.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public class ResultRequest {

    @NotBlank
    private LocalDate date;

    @Min(0)
    @Max(99)
    private int singleNumber;

    @Size(min = 1, max = 27)
    private List<Integer> numbers;

    private boolean force;

    public boolean isForce() {
        return force;
    }
    public void setForce(boolean force) {
        this.force = force;
    }

    // getter/setter
    public LocalDate getDate() {return date;}
    public int getSingleNumber() {return singleNumber;}
    public List<Integer> getNumbers() {return numbers;}

    public void setDate(LocalDate date) {this.date = date;}
    public void setSingleNumber(int singleNumber) {this.singleNumber = singleNumber;}
    public void setNumbers(List<Integer> numbers) { this.numbers = numbers;}
}