package com.ducdathua.prediction_app.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class PredictionSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String jsonData;

    private LocalDateTime createdAt;

    // constructor, getter

    public LocalDate getDate() { return date; }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public LocalDateTime getCreatedAt() {return  createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt=createdAt;}
}