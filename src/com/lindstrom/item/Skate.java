package com.lindstrom.item;

public class Skate extends Item {
    private int size;

    public Skate(String brand, int size) {
        super(brand);
        this.size = size;

    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Skridsko " + super.toString() +
                "storlek=" + size;
    }
}
