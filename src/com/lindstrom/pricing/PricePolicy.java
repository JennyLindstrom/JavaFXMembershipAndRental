package com.lindstrom.pricing;

public interface PricePolicy {
    double calculatePrice(double basePrice, int days);

}
