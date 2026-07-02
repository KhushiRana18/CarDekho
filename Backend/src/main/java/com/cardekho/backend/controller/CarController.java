package com.cardekho.backend.controller;

import com.cardekho.backend.model.Car;
import com.cardekho.backend.service.CarDataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {
    private final CarDataService carDataService;

    public CarController(CarDataService carDataService) {
        this.carDataService = carDataService;
    }

    @GetMapping
    public List<Car> getCars() {
        return carDataService.getCars();
    }
}
