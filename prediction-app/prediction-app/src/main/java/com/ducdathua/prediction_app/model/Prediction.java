package com.ducdathua.prediction_app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class Prediction {
    @Id
    @GeneratedValue
    private Long id;
    private LocalDate date;
    private int singleNumber;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
     public void setSingleNumber(int singleNumber) {
        this.singleNumber = singleNumber;
     }
}
