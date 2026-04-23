package com.ducdathua.prediction_app.dto;

public class NumberScoreDto {
    private int number;
    private double score;
    private double percentage;

    public NumberScoreDto() {}

    public NumberScoreDto(int number, double score, double percentage) {
        this.number = number;
        this.score = score;
        this.percentage = percentage;
    }

    public int getNumber() {return number;}
    public void setNumber(int number) {this.number = number;}

    public double getScore() {return score;}
    public void  setScore(double score) {this.score = score;}

    public double getPercentage() {return percentage;}
    public void setPercentage(double percentage) {this.percentage = percentage;}
}
