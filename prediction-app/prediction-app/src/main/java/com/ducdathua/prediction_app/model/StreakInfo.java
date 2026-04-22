package com.ducdathua.prediction_app.model;

public class StreakInfo {
    private int current;
    private int max;

    public StreakInfo() {
        this.current = 0;
        this.max = 0;
    }

    public int getCurrent() {
        return current;
    }

    public int getMax() {
        return max;
    }

    public void increase() {
        this.current++;
        if (this.current > this.max) {
            this.max = this.current;
        }
    }

    public void reset() {
        this.current = 0;
    }
}
