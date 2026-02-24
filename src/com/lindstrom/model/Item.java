package com.lindstrom.model;

import com.lindstrom.pricing.PricePolicy;

public abstract class Item {
    private final String id;
    private static int counter = 1;
    private String brand;
    private boolean available;

    public Item(String brand) {
        this.id = String.valueOf(counter++);
        this.brand = brand;
        this.available = true;
    }
    public abstract double getRentalCost(int days, PricePolicy pricePolicy);

    public String getId() {
        return id;
    }

    public String getBrand() {return brand; }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return ": " +
                "id='" + id + '\'' +
                ", märke='" + brand + '\'';
    }
}
