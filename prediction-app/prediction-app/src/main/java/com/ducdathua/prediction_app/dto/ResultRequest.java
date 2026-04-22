package com.ducdathua.prediction_app.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public class ResultRequest {

    @NotBlank
    private String date;

    @Min(0)
    @Max(99)
    private int singleNumber;

    @Size(min = 1, max = 27)
    private List<Integer> numbers;

    // getter/setter
    public String getDate() {return date;}
    public int getSingleNumber() {return singleNumber;}
    public List<Integer> getNumbers() {return numbers;}

    public void setDate(String date) {this.date = date;}
    public void setSingleNumber(int singleNumber) {this.singleNumber = singleNumber;}
    public void setNumbers(List<Integer> numbers) { this.numbers = numbers;}
}