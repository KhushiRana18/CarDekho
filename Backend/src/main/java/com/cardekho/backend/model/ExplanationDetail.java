package com.cardekho.backend.model;

import java.util.List;
import java.util.Map;

public class ExplanationDetail {
    private List<String> pros;
    private List<String> cons;
    private Map<String, Double> scoreBreakdown; // criterion -> score
    private List<String> matchReasons;

    public ExplanationDetail() {
    }

    public ExplanationDetail(List<String> pros, List<String> cons, Map<String, Double> scoreBreakdown, List<String> matchReasons) {
        this.pros = pros;
        this.cons = cons;
        this.scoreBreakdown = scoreBreakdown;
        this.matchReasons = matchReasons;
    }

    public List<String> getPros() {
        return pros;
    }

    public void setPros(List<String> pros) {
        this.pros = pros;
    }

    public List<String> getCons() {
        return cons;
    }

    public void setCons(List<String> cons) {
        this.cons = cons;
    }

    public Map<String, Double> getScoreBreakdown() {
        return scoreBreakdown;
    }

    public void setScoreBreakdown(Map<String, Double> scoreBreakdown) {
        this.scoreBreakdown = scoreBreakdown;
    }

    public List<String> getMatchReasons() {
        return matchReasons;
    }

    public void setMatchReasons(List<String> matchReasons) {
        this.matchReasons = matchReasons;
    }
}
