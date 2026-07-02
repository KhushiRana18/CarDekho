package com.cardekho.backend.model;

public class Car {
    private Long id;
    private String brand;
    private String model;
    private Double price;
    private String fuel;
    private String transmission;
    private String bodyType;
    private Double mileage;
    private Double power;
    private Double engine;
    private Integer seats;
    private Double bootSpace;
    private Double safetyScore;
    private Double maintenanceScore;
    private Double reliabilityScore;
    private Integer registrationYear;
    private String city;
    private String ownership;
    private Integer kilometersDriven;
    private String color;

    public Car() {
    }

    public Car(Long id, String brand, String model, Double price, String fuel, String transmission,
               String bodyType, Double mileage, Double power, Double engine, Integer seats,
               Double bootSpace, Double safetyScore, Double maintenanceScore, Double reliabilityScore,
               Integer registrationYear, String city, String ownership, Integer kilometersDriven, String color) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.price = price;
        this.fuel = fuel;
        this.transmission = transmission;
        this.bodyType = bodyType;
        this.mileage = mileage;
        this.power = power;
        this.engine = engine;
        this.seats = seats;
        this.bootSpace = bootSpace;
        this.safetyScore = safetyScore;
        this.maintenanceScore = maintenanceScore;
        this.reliabilityScore = reliabilityScore;
        this.registrationYear = registrationYear;
        this.city = city;
        this.ownership = ownership;
        this.kilometersDriven = kilometersDriven;
        this.color = color;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public String getFuel() { return fuel; }
    public void setFuel(String fuel) { this.fuel = fuel; }
    public String getTransmission() { return transmission; }
    public void setTransmission(String transmission) { this.transmission = transmission; }
    public String getBodyType() { return bodyType; }
    public void setBodyType(String bodyType) { this.bodyType = bodyType; }
    public Double getMileage() { return mileage; }
    public void setMileage(Double mileage) { this.mileage = mileage; }
    public Double getPower() { return power; }
    public void setPower(Double power) { this.power = power; }
    public Double getEngine() { return engine; }
    public void setEngine(Double engine) { this.engine = engine; }
    public Integer getSeats() { return seats; }
    public void setSeats(Integer seats) { this.seats = seats; }
    public Double getBootSpace() { return bootSpace; }
    public void setBootSpace(Double bootSpace) { this.bootSpace = bootSpace; }
    public Double getSafetyScore() { return safetyScore; }
    public void setSafetyScore(Double safetyScore) { this.safetyScore = safetyScore; }
    public Double getMaintenanceScore() { return maintenanceScore; }
    public void setMaintenanceScore(Double maintenanceScore) { this.maintenanceScore = maintenanceScore; }
    public Double getReliabilityScore() { return reliabilityScore; }
    public void setReliabilityScore(Double reliabilityScore) { this.reliabilityScore = reliabilityScore; }
    public Integer getRegistrationYear() { return registrationYear; }
    public void setRegistrationYear(Integer registrationYear) { this.registrationYear = registrationYear; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getOwnership() { return ownership; }
    public void setOwnership(String ownership) { this.ownership = ownership; }
    public Integer getKilometersDriven() { return kilometersDriven; }
    public void setKilometersDriven(Integer kilometersDriven) { this.kilometersDriven = kilometersDriven; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}
