package com.lindstrom.pricing;

import com.lindstrom.item.Item;

public interface PricePolicy {
    double calculatePrice(Item item, int days);

}
