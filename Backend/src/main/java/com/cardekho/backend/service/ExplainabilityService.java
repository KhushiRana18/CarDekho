package com.cardekho.backend.service;

import com.cardekho.backend.model.BuyerProfile;
import com.cardekho.backend.model.Car;
import com.cardekho.backend.model.ExplanationDetail;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExplainabilityService {

    public ExplanationDetail generateExplanation(Car car, BuyerProfile profile, Map<String, Double> scoreBreakdown) {
        List<String> pros = generateContextualPros(car, profile);
        List<String> cons = generateContextualCons(car, profile);
        List<String> reasons = generateMatchReasons(car, profile, scoreBreakdown);
        return new ExplanationDetail(pros, cons, scoreBreakdown, reasons);
    }

    private List<String> generateContextualPros(Car car, BuyerProfile profile) {
        List<String> pros = new ArrayList<>();

        // Mileage pros
        if (car.getMileage() != null) {
            if (car.getMileage() >= 20) {
                pros.add("Excellent fuel efficiency (" + String.format("%.1f", car.getMileage()) + " kmpl)");
            } else if (car.getMileage() >= 15) {
                pros.add("Good fuel economy (" + String.format("%.1f", car.getMileage()) + " kmpl)");
            }
        }

        // Safety
        if (car.getSafetyScore() != null && car.getSafetyScore() >= 8) {
            pros.add("Top-tier safety rating (" + String.format("%.1f", car.getSafetyScore()) + "/10)");
        } else if (car.getSafetyScore() != null && car.getSafetyScore() >= 7) {
            pros.add("Strong safety features (" + String.format("%.1f", car.getSafetyScore()) + "/10)");
        }

        // Maintenance
        if (car.getMaintenanceScore() != null && car.getMaintenanceScore() >= 8) {
            pros.add("Exceptional reliability with low maintenance");
        } else if (car.getMaintenanceScore() != null && car.getMaintenanceScore() >= 7) {
            pros.add("Good build quality and low maintenance");
        }

        // Reliability
        if (car.getReliabilityScore() != null && car.getReliabilityScore() >= 8) {
            pros.add("Highly reliable (" + String.format("%.1f", car.getReliabilityScore()) + "/10)");
        }

        // Price value
        if (profile.getBudget() != null && car.getPrice() != null) {
            double priceRatio = car.getPrice() / profile.getBudget();
            if (priceRatio < 0.7) {
                pros.add("Excellent value - well under your budget");
            } else if (priceRatio < 0.9) {
                pros.add("Good value within your budget");
            }
        }

        // Power if it's a priority
        if (car.getPower() != null && car.getPower() > 150) {
            pros.add("Strong performance with good power (" + String.format("%.0f", car.getPower()) + " bhp)");
        }

        // Boot space
        if (car.getBootSpace() != null && car.getBootSpace() > 400) {
            pros.add("Spacious trunk (" + String.format("%.0f", car.getBootSpace()) + " L)");
        }

        // Seats if family is priority
        if (car.getSeats() != null && car.getSeats() >= 7) {
            pros.add("Spacious 7-seater for family outings");
        }

        return pros.isEmpty() ? List.of("Solid choice within your preferences") : pros;
    }

    private List<String> generateContextualCons(Car car, BuyerProfile profile) {
        List<String> cons = new ArrayList<>();

        // Mileage cons
        if (car.getMileage() != null && car.getMileage() < 12) {
            cons.add("Lower fuel economy (" + String.format("%.1f", car.getMileage()) + " kmpl) - higher running costs");
        }

        // Power cons
        if (car.getPower() != null && car.getPower() < 100) {
            cons.add("Moderate performance (" + String.format("%.0f", car.getPower()) + " bhp)");
        }

        // Maintenance cons
        if (car.getMaintenanceScore() != null && car.getMaintenanceScore() < 6) {
            cons.add("Higher maintenance requirements expected");
        }

        // Safety cons
        if (car.getSafetyScore() != null && car.getSafetyScore() < 6) {
            cons.add("Below-average safety rating (" + String.format("%.1f", car.getSafetyScore()) + "/10)");
        }

        // Price cons
        if (profile.getBudget() != null && car.getPrice() != null && car.getPrice() > profile.getBudget()) {
            double excess = car.getPrice() - profile.getBudget();
            cons.add("Exceeds your budget by ₹" + String.format("%.0f", excess));
        }

        // Boot space cons
        if (car.getBootSpace() != null && car.getBootSpace() < 300) {
            cons.add("Limited trunk space (" + String.format("%.0f", car.getBootSpace()) + " L)");
        }

        // Seats cons
        if (car.getSeats() != null && car.getSeats() < 5) {
            cons.add("Only " + car.getSeats() + "-seater, limited capacity");
        }

        return cons;
    }

    private List<String> generateMatchReasons(Car car, BuyerProfile profile, Map<String, Double> scoreBreakdown) {
        List<String> reasons = new ArrayList<>();

        // Find top contributors
        double maxContrib = 0;
        String topCriteria = null;
        for (Map.Entry<String, Double> entry : scoreBreakdown.entrySet()) {
            if (entry.getValue() > maxContrib) {
                maxContrib = entry.getValue();
                topCriteria = entry.getKey();
            }
        }

        if (topCriteria != null && maxContrib > 0.1) {
            switch (topCriteria) {
                case "price" -> reasons.add("Strong match on price");
                case "mileage" -> reasons.add("Excellent fuel efficiency aligns with your priorities");
                case "safety" -> reasons.add("Safety rating is a major strength");
                case "maintenance" -> reasons.add("Low maintenance cost appeal");
                case "reliability" -> reasons.add("High reliability matches your requirements");
                case "power" -> reasons.add("Good performance characteristics");
                case "bootSpace" -> reasons.add("Spacious interior meets your needs");
            }
        }

        // Check fuel type match
        if (profile.getFuel() != null && profile.getFuel().equalsIgnoreCase(car.getFuel())) {
            reasons.add("Fuel type matches your preference (" + car.getFuel() + ")");
        }

        // Check transmission match
        if (profile.getTransmission() != null && profile.getTransmission().equalsIgnoreCase(car.getTransmission())) {
            reasons.add("Transmission preference met (" + car.getTransmission() + ")");
        }

        // Check body type match
        if (profile.getBodyType() != null && profile.getBodyType().equalsIgnoreCase(car.getBodyType())) {
            reasons.add("Body type preference matched (" + car.getBodyType() + ")");
        }

        return reasons.isEmpty() ? List.of("Good overall fit for your requirements") : reasons;
    }

    public List<String> generatePros(Car car) {
        List<String> pros = new ArrayList<>();
        if (car.getMileage() != null && car.getMileage() >= 18) {
            pros.add("Excellent mileage");
        }
        if (car.getSafetyScore() != null && car.getSafetyScore() >= 7) {
            pros.add("High safety score");
        }
        if (car.getMaintenanceScore() != null && car.getMaintenanceScore() >= 7) {
            pros.add("Low maintenance");
        }
        if (car.getReliabilityScore() != null && car.getReliabilityScore() >= 7) {
            pros.add("Reliable build quality");
        }
        if (car.getPrice() != null && car.getPrice() < 800000) {
            pros.add("Good value for money");
        }
        return pros;
    }

    public List<String> generateCons(Car car) {
        List<String> cons = new ArrayList<>();
        if (car.getPower() != null && car.getPower() < 100) {
            cons.add("Lower performance output");
        }
        if (car.getBootSpace() != null && car.getBootSpace() < 350) {
            cons.add("Smaller boot space");
        }
        if (car.getPrice() != null && car.getPrice() > 1000000) {
            cons.add("Higher price point");
        }
        if (car.getMaintenanceScore() != null && car.getMaintenanceScore() < 6) {
            cons.add("More maintenance effort");
        }
        return cons;
    }

    public List<String> compareAgainst(Car currentCar, Car otherCar) {
        List<String> comparison = new ArrayList<>();
        if (currentCar.getMileage() != null && otherCar.getMileage() != null && currentCar.getMileage() > otherCar.getMileage()) {
            comparison.add("+ Better mileage");
        }
        if (currentCar.getSafetyScore() != null && otherCar.getSafetyScore() != null && currentCar.getSafetyScore() > otherCar.getSafetyScore()) {
            comparison.add("+ Better safety score");
        }
        if (currentCar.getPrice() != null && otherCar.getPrice() != null && currentCar.getPrice() < otherCar.getPrice()) {
            comparison.add("+ Lower price");
        }
        if (currentCar.getPower() != null && otherCar.getPower() != null && currentCar.getPower() < otherCar.getPower()) {
            comparison.add("- Lower power output");
        }
        return comparison;
    }
}
