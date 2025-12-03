package com.lindstrom.pricing;

import com.lindstrom.item.Item;

public class JuniorPricePolicy implements PricePolicy{
    private static final double BASE_DAY_PRICE = 200.0;
    private static final double DISCOUNT = 0.2;

    @Override
    public double calculatePrice(Item item, int days) {
        return BASE_DAY_PRICE * (1.0 - DISCOUNT) * days;
    }
}
