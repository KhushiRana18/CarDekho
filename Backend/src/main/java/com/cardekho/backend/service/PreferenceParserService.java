package com.cardekho.backend.service;

import com.cardekho.backend.model.BuyerProfile;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PreferenceParserService {

    public BuyerProfile parse(String prompt) {
        BuyerProfile profile = new BuyerProfile();
        profile.setBudget(extractBudget(prompt));
        profile.setFuel(extractFuel(prompt));
        profile.setTransmission(extractTransmission(prompt));
        profile.setBodyType(extractBodyType(prompt));
        profile.setUsage(extractUsage(prompt));
        profile.setFamilySize(extractFamilySize(prompt));

        Map<String, Integer> importance = new HashMap<>();
        importance.put("mileage", extractImportance(prompt, "mileage", 7));
        importance.put("safety", extractImportance(prompt, "safety", 6));
        importance.put("maintenance", extractImportance(prompt, "maintenance", 6));
        importance.put("comfort", extractImportance(prompt, "comfort", 5));
        importance.put("performance", extractImportance(prompt, "performance", 3));
        profile.setImportance(importance);
        return profile;
    }

    private Double extractBudget(String prompt) {
        if (prompt == null) {
            return 1200000.0;
        }
        var matcher = java.util.regex.Pattern.compile("(\\d+)(?:\\s*(?:lakh|lac|k|thousand))").matcher(prompt.toLowerCase());
        if (matcher.find()) {
            double value = Double.parseDouble(matcher.group(1));
            if (prompt.toLowerCase().contains("lakh") || prompt.toLowerCase().contains("lac")) {
                return value * 100000;
            }
            if (prompt.toLowerCase().contains("k")) {
                return value * 1000;
            }
            return value;
        }
        return 1200000.0;
    }

    private String extractFuel(String prompt) {
        if (prompt == null) return "Petrol";
        String lower = prompt.toLowerCase();
        if (lower.contains("diesel")) return "Diesel";
        if (lower.contains("electric")) return "Electric";
        if (lower.contains("cng")) return "CNG";
        return "Petrol";
    }

    private String extractTransmission(String prompt) {
        if (prompt == null) return "Automatic";
        String lower = prompt.toLowerCase();
        if (lower.contains("manual")) return "Manual";
        return "Automatic";
    }

    private String extractBodyType(String prompt) {
        if (prompt == null) return "SUV";
        String lower = prompt.toLowerCase();
        if (lower.contains("sedan")) return "Sedan";
        if (lower.contains("hatchback")) return "Hatchback";
        if (lower.contains("suv")) return "SUV";
        return "SUV";
    }

    private String extractUsage(String prompt) {
        if (prompt == null) return "City";
        String lower = prompt.toLowerCase();
        if (lower.contains("highway") || lower.contains("travel")) return "Highway";
        if (lower.contains("family") || lower.contains("family use")) return "Family";
        return "City";
    }

    private Integer extractFamilySize(String prompt) {
        if (prompt == null) return 4;
        var matcher = java.util.regex.Pattern.compile("\\b(\\d+)\\s*(?:people|members|family|persons)\\b").matcher(prompt.toLowerCase());
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 4;
    }

    private Integer extractImportance(String prompt, String feature, int defaultValue) {
        if (prompt == null || prompt.isBlank()) {
            return defaultValue;
        }
        String lower = prompt.toLowerCase();
        if (lower.contains(feature)) {
            return Math.max(defaultValue, 8);
        }
        return defaultValue;
    }
}
