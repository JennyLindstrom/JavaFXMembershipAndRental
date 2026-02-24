package com.lindstrom.pricing;

import com.lindstrom.model.Item;

public class StandardPricePolicy implements PricePolicy {


    @Override
    public double calculatePrice(double basePrice, int days) {
        return basePrice * days;
    }
}
