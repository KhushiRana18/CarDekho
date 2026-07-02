package com.cardekho.backend;

import com.cardekho.backend.model.BuyerProfile;
import com.cardekho.backend.model.Car;
import com.cardekho.backend.service.FilterService;
import com.cardekho.backend.service.SemanticSimilarityService;
import com.cardekho.backend.service.TopsisService;
import com.cardekho.backend.service.WeightGeneratorService;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RecommendationPipelineTest {

    @Test
    void weightGeneratorShouldNormalizeImportanceValues() {
        BuyerProfile profile = new BuyerProfile();
        Map<String, Integer> importance = new HashMap<>();
        importance.put("mileage", 8);
        importance.put("safety", 5);
        importance.put("maintenance", 7);
        profile.setImportance(importance);

        WeightGeneratorService service = new WeightGeneratorService();
        Map<String, Double> weights = service.generateWeights(profile);

        assertEquals(0.4, weights.get("mileage"), 0.01);
        assertEquals(0.25, weights.get("safety"), 0.01);
        assertEquals(0.35, weights.get("maintenance"), 0.01);
    }

    @Test
    void filterServiceShouldApplyHardConstraints() {
        BuyerProfile profile = new BuyerProfile();
        profile.setBudget(1000000.0);
        profile.setFuel("Petrol");
        profile.setTransmission("Automatic");
        profile.setBodyType("SUV");

        List<Car> cars = List.of(
                createCar("Petrol", "Automatic", "SUV", 900000.0),
                createCar("Diesel", "Automatic", "SUV", 900000.0),
                createCar("Petrol", "Manual", "SUV", 900000.0),
                createCar("Petrol", "Automatic", "Sedan", 900000.0),
                createCar("Petrol", "Automatic", "SUV", 1100000.0)
        );

        FilterService service = new FilterService();
        List<Car> filtered = service.filterCars(cars, profile);

        assertEquals(1, filtered.size());
        assertEquals("Petrol", filtered.get(0).getFuel());
    }

    @Test
    void topsisServiceShouldRankHigherCars() {
        Car betterCar = createCar("Petrol", "Automatic", "SUV", 900000.0);
        betterCar.setId(1L);
        betterCar.setMileage(20.0);
        betterCar.setPower(120.0);
        betterCar.setSafetyScore(8.0);
        betterCar.setMaintenanceScore(8.0);
        betterCar.setReliabilityScore(8.0);
        betterCar.setBootSpace(450.0);

        Car weakerCar = createCar("Petrol", "Automatic", "SUV", 1100000.0);
        weakerCar.setId(2L);
        weakerCar.setMileage(14.0);
        weakerCar.setPower(90.0);
        weakerCar.setSafetyScore(5.0);
        weakerCar.setMaintenanceScore(5.0);
        weakerCar.setReliabilityScore(4.0);
        weakerCar.setBootSpace(350.0);

        TopsisService service = new TopsisService();
        Map<String, Double> weights = Map.of("mileage", 0.4, "power", 0.2, "safety", 0.2, "maintenance", 0.1, "reliability", 0.1);
        Map<Car, Double> ranked = service.rankCars(List.of(betterCar, weakerCar), weights);
        List<Car> orderedCars = ranked.keySet().stream().toList();

        assertEquals(betterCar, orderedCars.get(0));
    }

    @Test
    void semanticSimilarityShouldReturnPositiveScore() {
        SemanticSimilarityService service = new SemanticSimilarityService();
        double score = service.score("comfortable reliable family suv", "comfortable family suv with reliable engine");
        assertTrue(score > 0.0);
    }

    private Car createCar(String fuel, String transmission, String bodyType, double price) {
        Car car = new Car();
        car.setFuel(fuel);
        car.setTransmission(transmission);
        car.setBodyType(bodyType);
        car.setPrice(price);
        return car;
    }
}
