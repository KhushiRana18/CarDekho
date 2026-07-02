package com.cardekho.backend.service;

import com.cardekho.backend.model.Car;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Service
public class CarDataService {
    private final List<Car> cars = new ArrayList<>();

    public CarDataService() {
        loadCars();
    }

    public List<Car> getCars() {
        return cars;
    }

    private void loadCars() {
        try {
            ClassPathResource resource = new ClassPathResource("data/car_prices.csv");
            try (CSVParser parser = CSVFormat.DEFAULT
                    .builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .build()
                    .parse(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

                int index = 0;
                for (CSVRecord record : parser) {
                    Car car = new Car();
                    car.setId((long) ++index);
                    car.setBrand(record.get("Car_Model").split(" ")[0]);
                    car.setModel(record.get("Car_Model"));
                    car.setPrice(parseDouble(record.get("Price")));
                    car.setFuel(record.get("Fuel_Type"));
                    car.setTransmission(record.get("transmission"));
                    car.setBodyType(record.get("Body_Type"));
                    car.setMileage(parseDouble(record.get("Mileage")));
                    car.setPower(parseDouble(record.get("Engine_Power")));
                    car.setEngine(parseDouble(record.get("Engine_Power")));
                    car.setSeats(5);
                    car.setBootSpace(400.0);
                    car.setSafetyScore(5.0);
                    car.setMaintenanceScore(7.0);
                    car.setReliabilityScore(7.0);
                    car.setRegistrationYear(parseInt(record.get("Registration_Year")));
                    car.setCity(record.get("City"));
                    car.setOwnership(record.get("Ownership"));
                    car.setKilometersDriven(parseInt(record.get("Kilometers_Driven")));
                    car.setColor(record.get("color"));
                    cars.add(car);
                }

                fillMissingMileage();
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to load car dataset", ex);
        }
    }

    private void fillMissingMileage() {
        OptionalDouble averageMileage = cars.stream()
                .mapToDouble(car -> car.getMileage() == null ? 0.0 : car.getMileage())
                .filter(value -> value > 0.0)
                .average();

        if (averageMileage.isEmpty()) {
            return;
        }

        double meanMileage = averageMileage.getAsDouble();
        cars.forEach(car -> {
            if (car.getMileage() == null || car.getMileage() <= 0.0) {
                car.setMileage(meanMileage);
            }
        });
    }

    private Double parseDouble(String value) {
        if (value == null || value.isBlank()) {
            return 0.0;
        }
        return Double.parseDouble(value);
    }

    private Integer parseInt(String value) {
        if (value == null || value.isBlank()) {
            return 0;
        }
        return Integer.parseInt(value);
    }
}
