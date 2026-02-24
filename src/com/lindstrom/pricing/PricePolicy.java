package com.lindstrom.pricing;

import com.lindstrom.model.Item;

public interface PricePolicy {
    double calculatePrice(double basePrice, int days);

}
