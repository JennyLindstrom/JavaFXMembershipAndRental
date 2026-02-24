package com.lindstrom.repository;

import com.lindstrom.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Inventory {
    private List<Item> items = new ArrayList<>();

    public void addItem(Item item) {items.add(item);}
    public List<Item> getAllItems() {return items;}

    public List<Item> getAvilableItems() {
        return items.stream()
                .filter(Item::isAvailable)
                .collect(Collectors.toList());
    }


}
