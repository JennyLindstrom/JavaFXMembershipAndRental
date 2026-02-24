package com.lindstrom.model;

import com.lindstrom.pricing.PricePolicy;

public class Helmet extends Item {

    private String size;

    public Helmet(String brand, String size) {
        super(brand);
        this.size = size;
    }


    public String getSize() {
        return size;
    }


    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public double getRentalCost(int days, PricePolicy pricePolicy) {
        return pricePolicy.calculatePrice(100, days);
    }
    @Override
    public String toString() {
        return "Hjälm" + super.toString() +
                "storlek='" + size + '\'';
    }

}
