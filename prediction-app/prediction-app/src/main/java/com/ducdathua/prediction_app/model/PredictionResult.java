package com.ducdathua.prediction_app.model;

import com.ducdathua.prediction_app.dto.NumberScoreDto;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class PredictionResult {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private LocalDate date;

    @ElementCollection
    private List<Integer> top3;

    @ElementCollection
    private List<Integer> top15;

    @ElementCollection
    private List<PredictionNumber> full;

    public PredictionResult() {
    }

    public PredictionResult(LocalDate date, List<Integer> top3, List<Integer> top15,
                            List<PredictionNumber> full) {
        this.date = date;
        this.top3 = top3;
        this.top15 = top15;
        this.full = full;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<Integer> getTop3() {
        return top3;
    }

    public List<Integer> getTop15() {
        return top15;
    }

    public void setTop3(List<Integer> top3) {
        this.top3 = top3;
    }

    public void setTop15(List<Integer> top15) {
        this.top15 = top15;
    }


    public List<PredictionNumber> getFull() {return full;}
    public void setFull(List<PredictionNumber> full) {this.full = full;}
}

