package com.cardekho.backend.model;

import java.util.Map;

public class RankedCar {
    private Car car;
    private Double totalScore;
    private Map<String, Double> scoreBreakdown;

    public RankedCar(Car car, Double totalScore, Map<String, Double> scoreBreakdown) {
        this.car = car;
        this.totalScore = totalScore;
        this.scoreBreakdown = scoreBreakdown;
    }

    public Car getCar() {
        return car;
    }

    public Double getTotalScore() {
        return totalScore;
    }

    public Map<String, Double> getScoreBreakdown() {
        return scoreBreakdown;
    }
}
