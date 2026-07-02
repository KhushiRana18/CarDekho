package com.cardekho.backend.service;

import com.cardekho.backend.model.BuyerProfile;
import com.cardekho.backend.model.Car;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilterService {

    public List<Car> filterCars(List<Car> cars, BuyerProfile profile) {
        if (cars == null || cars.isEmpty()) {
            return List.of();
        }

        return cars.stream()
                .filter(car -> profile.getBudget() == null || car.getPrice() <= profile.getBudget())
                .filter(car -> profile.getFuel() == null || profile.getFuel().isBlank() || car.getFuel().equalsIgnoreCase(profile.getFuel()))
                .filter(car -> profile.getTransmission() == null || profile.getTransmission().isBlank() || car.getTransmission().equalsIgnoreCase(profile.getTransmission()))
                .filter(car -> profile.getBodyType() == null || profile.getBodyType().isBlank() || car.getBodyType().equalsIgnoreCase(profile.getBodyType()))
                .collect(Collectors.toList());
    }
}
