package com.cardekho.backend.service;

import com.cardekho.backend.model.Car;
import com.cardekho.backend.model.RankedCar;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TopsisService {

    public Map<Car, Double> rankCars(List<Car> cars, Map<String, Double> weights) {
        Map<String, Double> mileage = normalize(cars, "mileage");
        Map<String, Double> price = normalize(cars, "price");
        Map<String, Double> power = normalize(cars, "power");
        Map<String, Double> safety = normalize(cars, "safety");
        Map<String, Double> maintenance = normalize(cars, "maintenance");
        Map<String, Double> reliability = normalize(cars, "reliability");
        Map<String, Double> bootSpace = normalize(cars, "bootSpace");

        Map<Car, Double> scores = new LinkedHashMap<>();
        for (Car car : cars) {
            String id = car.getId().toString();
            double weightedScore = 0.0;
            weightedScore += weights.getOrDefault("mileage", 0.0) * mileage.getOrDefault(id, 0.0);
            weightedScore += weights.getOrDefault("price", 0.0) * price.getOrDefault(id, 0.0);
            weightedScore += weights.getOrDefault("power", 0.0) * power.getOrDefault(id, 0.0);
            weightedScore += weights.getOrDefault("safety", 0.0) * safety.getOrDefault(id, 0.0);
            weightedScore += weights.getOrDefault("maintenance", 0.0) * maintenance.getOrDefault(id, 0.0);
            weightedScore += weights.getOrDefault("reliability", 0.0) * reliability.getOrDefault(id, 0.0);
            weightedScore += weights.getOrDefault("bootSpace", 0.0) * bootSpace.getOrDefault(id, 0.0);
            scores.put(car, weightedScore);
        }

        return scores.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
    }

    public List<RankedCar> rankCarsWithDetails(List<Car> cars, Map<String, Double> weights) {
        Map<String, Double> mileageNorm = normalize(cars, "mileage");
        Map<String, Double> priceNorm = normalize(cars, "price");
        Map<String, Double> powerNorm = normalize(cars, "power");
        Map<String, Double> safetyNorm = normalize(cars, "safety");
        Map<String, Double> maintenanceNorm = normalize(cars, "maintenance");
        Map<String, Double> reliabilityNorm = normalize(cars, "reliability");
        Map<String, Double> bootSpaceNorm = normalize(cars, "bootSpace");

        List<RankedCar> rankedCars = new java.util.ArrayList<>();
        for (Car car : cars) {
            String id = car.getId().toString();
            Map<String, Double> breakdown = new HashMap<>();
            double weightedScore = 0.0;

            double mileageContrib = weights.getOrDefault("mileage", 0.0) * mileageNorm.getOrDefault(id, 0.0);
            breakdown.put("mileage", mileageContrib);
            weightedScore += mileageContrib;

            double priceContrib = weights.getOrDefault("price", 0.0) * priceNorm.getOrDefault(id, 0.0);
            breakdown.put("price", priceContrib);
            weightedScore += priceContrib;

            double powerContrib = weights.getOrDefault("power", 0.0) * powerNorm.getOrDefault(id, 0.0);
            breakdown.put("power", powerContrib);
            weightedScore += powerContrib;

            double safetyContrib = weights.getOrDefault("safety", 0.0) * safetyNorm.getOrDefault(id, 0.0);
            breakdown.put("safety", safetyContrib);
            weightedScore += safetyContrib;

            double maintenanceContrib = weights.getOrDefault("maintenance", 0.0) * maintenanceNorm.getOrDefault(id, 0.0);
            breakdown.put("maintenance", maintenanceContrib);
            weightedScore += maintenanceContrib;

            double reliabilityContrib = weights.getOrDefault("reliability", 0.0) * reliabilityNorm.getOrDefault(id, 0.0);
            breakdown.put("reliability", reliabilityContrib);
            weightedScore += reliabilityContrib;

            double bootSpaceContrib = weights.getOrDefault("bootSpace", 0.0) * bootSpaceNorm.getOrDefault(id, 0.0);
            breakdown.put("bootSpace", bootSpaceContrib);
            weightedScore += bootSpaceContrib;

            rankedCars.add(new RankedCar(car, weightedScore, breakdown));
        }

        return rankedCars.stream()
                .sorted((a, b) -> b.getTotalScore().compareTo(a.getTotalScore()))
                .collect(Collectors.toList());
    }

    private Map<String, Double> normalize(List<Car> cars, String fieldName) {
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;
        for (Car car : cars) {
            Double value = getValue(car, fieldName);
            if (value != null) {
                min = Math.min(min, value);
                max = Math.max(max, value);
            }
        }
        if (Double.isInfinite(min) || Double.isInfinite(max)) {
            return Map.of();
        }
        double range = max - min;
        Map<String, Double> normalized = new LinkedHashMap<>();
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
