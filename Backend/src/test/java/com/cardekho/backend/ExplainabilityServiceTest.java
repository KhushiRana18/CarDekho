package com.cardekho.backend;

import com.cardekho.backend.model.Car;
import com.cardekho.backend.service.ExplainabilityService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExplainabilityServiceTest {

    @Test
    void shouldGenerateProsAndCons() {
        Car car = new Car();
        car.setMileage(20.0);
        car.setSafetyScore(8.0);
        car.setMaintenanceScore(8.0);
        car.setReliabilityScore(8.0);
        car.setPrice(700000.0);
        car.setPower(90.0);
        car.setBootSpace(300.0);

        ExplainabilityService service = new ExplainabilityService();
        List<String> pros = service.generatePros(car);
        List<String> cons = service.generateCons(car);

        assertTrue(pros.contains("Excellent mileage"));
        assertTrue(cons.contains("Smaller boot space"));
        assertFalse(cons.isEmpty());
    }
}
