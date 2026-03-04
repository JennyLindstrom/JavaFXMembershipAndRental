package com.lindstrom.model;

import com.lindstrom.pricing.PricePolicy;

public class Skate extends Item {
    private String size;

    public Skate(String brand, String size) {
        super(brand);
        this.size = size;

    }

    @Override
    public double getRentalCost(int days, PricePolicy pricePolicy) {
        return pricePolicy.calculatePrice(150, days);
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Skridsko " + super.toString() +
                "storlek=" + size;
    }
}
