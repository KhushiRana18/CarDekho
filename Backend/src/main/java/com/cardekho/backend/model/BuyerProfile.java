package com.cardekho.backend.model;

import java.util.HashMap;
import java.util.Map;

public class BuyerProfile {
    private Double budget;
    private String fuel;
    private String transmission;
    private String bodyType;
    private String usage;
    private Integer familySize;
    private Map<String, Integer> importance = new HashMap<>();

    public BuyerProfile() {
    }

    public Double getBudget() { return budget; }
    public void setBudget(Double budget) { this.budget = budget; }
    public String getFuel() { return fuel; }
    public void setFuel(String fuel) { this.fuel = fuel; }
    public String getTransmission() { return transmission; }
    public void setTransmission(String transmission) { this.transmission = transmission; }
    public String getBodyType() { return bodyType; }
    public void setBodyType(String bodyType) { this.bodyType = bodyType; }
    public String getUsage() { return usage; }
    public void setUsage(String usage) { this.usage = usage; }
    public Integer getFamilySize() { return familySize; }
    public void setFamilySize(Integer familySize) { this.familySize = familySize; }
    public Map<String, Integer> getImportance() { return importance; }
    public void setImportance(Map<String, Integer> importance) { this.importance = importance; }
}
