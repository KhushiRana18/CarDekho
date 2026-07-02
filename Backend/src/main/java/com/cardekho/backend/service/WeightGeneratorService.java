package com.cardekho.backend.service;

import com.cardekho.backend.model.BuyerProfile;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class WeightGeneratorService {

    public Map<String, Double> generateWeights(BuyerProfile profile) {
        Map<String, Integer> importance = profile.getImportance();
        if (importance == null || importance.isEmpty()) {
            return defaultWeights();
        }

        int total = importance.values().stream().mapToInt(Integer::intValue).sum();
        if (total <= 0) {
            return defaultWeights();
        }

        Map<String, Double> weights = new LinkedHashMap<>();
        importance.forEach((feature, value) -> weights.put(feature, value / (double) total));
        return weights;
    }

    private Map<String, Double> defaultWeights() {
        Map<String, Double> weights = new LinkedHashMap<>();
        weights.put("mileage", 0.35);
        weights.put("safety", 0.25);
        weights.put("maintenance", 0.20);
        weights.put("comfort", 0.10);
        weights.put("performance", 0.10);
        return weights;
    }
}
