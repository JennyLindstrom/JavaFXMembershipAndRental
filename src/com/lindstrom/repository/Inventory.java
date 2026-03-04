package com.lindstrom.repository;

import com.lindstrom.model.Helmet;
import com.lindstrom.model.HockeyStick;
import com.lindstrom.model.Item;
import com.lindstrom.model.Skate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Inventory {
    private final List<Item> items = new ArrayList<>();

    public void addItem(Item item) {
        items.add(item);
    }

    public List<Item> getAllItems() {
        return items;
    }

    public List<Item> getAvilableItems() {
        return items.stream()
                .filter(Item::isAvailable)
                .collect(Collectors.toList());
    }

    public void loadFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String type = parts[0];
                    String brand = parts[1];

                    switch (type) {
                        case "Helmet":
                            if (parts.length >= 3) {
                                items.add(new Helmet(parts[2], brand));
                            }
                            break;
                        case "HockeyStick":
                            if (parts.length >= 5) {
                                items.add(new HockeyStick(brand, parts[2], parts[3], parts[4]));
                            }
                            break;
                        case "Skate":
                            if (parts.length >= 3) {
                                items.add(new Skate(brand, parts[2]));
                            }
                            break;
                    }
                }
            }
        }
    }

    public void saveToFile(String filename) throws IOException {
        try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(filename))) {
            System.out.println("Sparar " + items.size() + " artiklar...");
            for (Item item : items) {
                if (item instanceof Helmet) {
                    Helmet helmet = (Helmet) item;
                    writer.println("Helmet, " + helmet.getBrand() + ", " + helmet.getSize());
                    System.out.println("Sparad: Hjälm" + helmet.getBrand());
                } else if (item instanceof HockeyStick) {
                    HockeyStick hockeyStick = (HockeyStick) item;
                    writer.println("HockeyStick, " + hockeyStick.getBrand() + ", " + hockeyStick.getMaterial() +
                            ", " + hockeyStick.getFlex() + ", " + hockeyStick.getHand());
                    System.out.println("Sparad Hockeyklubba " + hockeyStick.getBrand());
                } else if (item instanceof Skate) {
                    Skate skate = (Skate) item;
                    writer.println("Skate, " + skate.getBrand() + ", " + skate.getSize());
                    System.out.println("Sparad Skridsko " + skate.getBrand());
                }
            }
            System.out.println("Allt är sparat till " + filename + "!");
        }
    }
}
