package com.lindstrom.item;

public abstract class Item {
    private final String id;
    private static int counter = 1;
    private String brand;
    private boolean available;

    public Item(String brand) {
        this.id = String.valueOf(counter++);
        this.brand = brand;
        this.available = true;
    }

    public String getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return ": " +
                "id='" + id + '\'' +
                ", märke='" + brand + '\'';
    }
}
