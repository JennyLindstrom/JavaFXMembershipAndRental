package com.lindstrom.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory {
    private final Map<String, Item> items = new HashMap<>();

    public void addItem(Item item) {
        items.put(item.getId(), item);
    }

    public Item getItemById(String id) {
        return items.get(id);
    }

    public void removeItem(String id) {
        items.remove(id);
    }


    public <T extends Item> List<T> filterByType(Class<T> type) {
        List<T> result = new ArrayList<>();
        for (Item item : items.values()) {
            if (type.isInstance(item)) {
                result.add(type.cast(item));
            }
        }
        return result;
    }
}
