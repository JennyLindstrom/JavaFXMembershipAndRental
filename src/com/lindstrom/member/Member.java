package com.lindstrom.member;

import java.util.ArrayList;
import java.util.List;

public class Member {
    private final int id;
    private final String name;
    private MemberRegistry.StatusLevel statusLevel;
    private final List<Rental> rentalHistory;


    public Member(int id, String name, MemberRegistry.StatusLevel statusLevel) {
        this.id = id;
        this.name = name;
        this.statusLevel = statusLevel;
        this.rentalHistory = new ArrayList<>();
    }

    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public MemberRegistry.StatusLevel getStatusLevel() {
        return statusLevel;
    }

    public void setStatusLevel(MemberRegistry.StatusLevel statusLevel) {
        this.statusLevel = statusLevel;
    }

    public List<Rental> getRentalHistory() {
        return rentalHistory;
    }

    public void addRentalToHistory(Rental rental) {
        rentalHistory.add(rental);
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Namn: " + name + ", Status: " + statusLevel;
    }
}

