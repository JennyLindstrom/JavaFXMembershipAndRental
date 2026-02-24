package com.lindstrom.model;

import com.lindstrom.pricing.PricePolicy;

public class Skate extends Item {
    private int size;

    public Skate(String brand, int size) {
        super(brand);
        this.size = size;

    }
    @Override
    public double getRentalCost(int days, PricePolicy pricePolicy) {
        return pricePolicy.calculatePrice(150, days);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Skridsko " + super.toString() +
                "storlek=" + size;
    }
}
