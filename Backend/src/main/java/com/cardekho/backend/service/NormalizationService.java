package com.cardekho.backend.service;

import com.cardekho.backend.model.Car;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NormalizationService {

    public Map<String, Double> normalizeFeature(List<Car> cars, String fieldName) {
        List<Double> values = new ArrayList<>();
        for (Car car : cars) {
            Double value = getValue(car, fieldName);
            if (value != null) {
                values.add(value);
            }
        }

        if (values.isEmpty()) {
            return new HashMap<>();
        }

        double min = values.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
        double max = values.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
        double range = max - min;

        Map<String, Double> normalized = new HashMap<>();
        for (Car car : cars) {
            Double value = getValue(car, fieldName);
            if (value == null) {
                normalized.put(car.getId().toString(), 0.0);
            } else if (range == 0) {
                normalized.put(car.getId().toString(), 1.0);
            } else {
                normalized.put(car.getId().toString(), (value - min) / range);
            }
        }
        return normalized;
    }

    private Double getValue(Car car, String fieldName) {
        return switch (fieldName) {
            case "price" -> car.getPrice();
            case "mileage" -> car.getMileage();
            case "power" -> car.getPower();
            case "safety" -> car.getSafetyScore();
            case "maintenance" -> car.getMaintenanceScore();
            case "reliability" -> car.getReliabilityScore();
            case "bootSpace" -> car.getBootSpace();
            default -> null;
        };
    }
}
