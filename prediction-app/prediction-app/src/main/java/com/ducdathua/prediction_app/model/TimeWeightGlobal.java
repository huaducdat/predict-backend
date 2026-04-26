package com.ducdathua.prediction_app.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "time_weight_global")
public class TimeWeightGlobal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String data;

    public TimeWeightGlobal() {}

    public TimeWeightGlobal(LocalDate date, String data) {
        this.date = date;
        this.data = data;
    }

    // getter/setter

    public Long getId() {return id;}
    public LocalDate getDate() {return date;}
    public void setDate(LocalDate date) {this.date = date;}

    public String getData() {return data;}
    public void setData(String data) {this.data = data;}


}