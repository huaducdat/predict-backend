package com.ducdathua.prediction_app.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pair_global")
public class PairGlobal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String data; // JSON

    public PairGlobal() {}

    public PairGlobal(LocalDate date, String data) {
        this.date = date;
        this.data = data;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getData() {
        return data;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setData(String data) {
        this.data = data;
    }
}