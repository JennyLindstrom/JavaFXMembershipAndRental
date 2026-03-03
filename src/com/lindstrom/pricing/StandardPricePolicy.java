package com.lindstrom.pricing;

public class StandardPricePolicy implements PricePolicy {


    @Override
    public double calculatePrice(double basePrice, int days) {
        return basePrice * days;
    }
}
