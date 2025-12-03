package com.lindstrom.pricing;

import com.lindstrom.item.Item;

public class StandardPricePolicy implements PricePolicy {
    private static final double BASE_DAY_PRICE = 200.0;

    @Override
    public double calculatePrice(Item item, int days) {
        return BASE_DAY_PRICE * days;
    }
}
