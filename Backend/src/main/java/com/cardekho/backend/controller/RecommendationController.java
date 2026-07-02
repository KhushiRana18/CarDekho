package com.cardekho.backend.controller;

import com.cardekho.backend.model.BuyerProfile;
import com.cardekho.backend.model.Car;
import com.cardekho.backend.service.CarDataService;
import com.cardekho.backend.service.ExplainabilityService;
import com.cardekho.backend.service.FilterService;
import com.cardekho.backend.service.PreferenceParserService;
import com.cardekho.backend.service.SemanticSimilarityService;
import com.cardekho.backend.service.TopsisService;
import com.cardekho.backend.service.WeightGeneratorService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recommend")
public class RecommendationController {
    private final CarDataService carDataService;
    private final PreferenceParserService preferenceParserService;
    private final WeightGeneratorService weightGeneratorService;
    private final FilterService filterService;
    private final TopsisService topsisService;
    private final SemanticSimilarityService semanticSimilarityService;
    private final ExplainabilityService explainabilityService;

    public RecommendationController(CarDataService carDataService, PreferenceParserService preferenceParserService,
                                     WeightGeneratorService weightGeneratorService, FilterService filterService,
                                     TopsisService topsisService, SemanticSimilarityService semanticSimilarityService,
                                     ExplainabilityService explainabilityService) {
        this.carDataService = carDataService;
        this.preferenceParserService = preferenceParserService;
        this.weightGeneratorService = weightGeneratorService;
        this.filterService = filterService;
        this.topsisService = topsisService;
        this.semanticSimilarityService = semanticSimilarityService;
        this.explainabilityService = explainabilityService;
    }

    @PostMapping
    public Map<String, Object> recommend(@RequestBody Map<String, Object> request) {
        String prompt = String.valueOf(request.getOrDefault("prompt", ""));
        BuyerProfile buyerProfile = preferenceParserService.parse(prompt);

        if (request.containsKey("budget")) {
            buyerProfile.setBudget(Double.valueOf(String.valueOf(request.get("budget"))));
        }
        if (request.containsKey("fuel")) {
            buyerProfile.setFuel(String.valueOf(request.get("fuel")));
        }
        if (request.containsKey("transmission")) {
            buyerProfile.setTransmission(String.valueOf(request.get("transmission")));
        }
        if (request.containsKey("bodyType")) {
            buyerProfile.setBodyType(String.valueOf(request.get("bodyType")));
        }

        List<Car> filtered = filterService.filterCars(carDataService.getCars(), buyerProfile);
        Map<String, Double> weights = weightGeneratorService.generateWeights(buyerProfile);
        
        // Get ranked cars with score breakdown
        var rankedCars = topsisService.rankCarsWithDetails(filtered, weights);

        Map<String, Object> response = new HashMap<>();
        response.put("buyerProfile", buyerProfile);
        response.put("recommendations", rankedCars.stream().limit(5).map(ranked -> {
            Car car = ranked.getCar();
            double topsisScore = ranked.getTotalScore();
            double semanticScore = semanticSimilarityService.score(prompt, buildCarDescription(car));
            double finalScore = 0.7 * topsisScore + 0.3 * semanticScore;
            
            // Generate context-aware explanation
            var explanation = explainabilityService.generateExplanation(car, buyerProfile, ranked.getScoreBreakdown());
            
            Map<String, Object> item = new HashMap<>();
            
            // Core info
            item.put("id", car.getId());
            item.put("car", car.getBrand() + " " + car.getModel());
            item.put("brand", car.getBrand());
            item.put("model", car.getModel());
            
            // Specifications
            item.put("price", car.getPrice());
            item.put("fuel", car.getFuel());
            item.put("transmission", car.getTransmission());
            item.put("bodyType", car.getBodyType());
            item.put("mileage", car.getMileage());
            item.put("power", car.getPower());
            item.put("engine", car.getEngine());
            item.put("seats", car.getSeats());
            item.put("bootSpace", car.getBootSpace());
            item.put("safetyScore", car.getSafetyScore());
            item.put("maintenanceScore", car.getMaintenanceScore());
            item.put("reliabilityScore", car.getReliabilityScore());
            
            // Additional details
            item.put("registrationYear", car.getRegistrationYear());
            item.put("city", car.getCity());
            item.put("ownership", car.getOwnership());
            item.put("kilometersDriven", car.getKilometersDriven());
            item.put("color", car.getColor());
            
            // Scoring
            item.put("topsisScore", topsisScore);
            item.put("semanticScore", semanticScore);
            item.put("matchScore", finalScore);
            item.put("scoreBreakdown", ranked.getScoreBreakdown());
            
            // Explanations
            item.put("pros", explanation.getPros());
            item.put("cons", explanation.getCons());
            item.put("matchReasons", explanation.getMatchReasons());
            
            return item;
        }).toList());
        return response;
    }

    private String buildCarDescription(Car car) {
        return String.join(" ",
                car.getBodyType() != null ? car.getBodyType() : "car",
                car.getFuel() != null ? car.getFuel() : "vehicle",
                car.getTransmission() != null ? car.getTransmission() : "drive",
                car.getMileage() != null && car.getMileage() > 0 ? "good mileage" : "reliable",
                car.getReliabilityScore() != null && car.getReliabilityScore() >= 7 ? "reliable" : "practical",
                car.getMaintenanceScore() != null && car.getMaintenanceScore() >= 7 ? "low maintenance" : "economical"
        );
    }
}
