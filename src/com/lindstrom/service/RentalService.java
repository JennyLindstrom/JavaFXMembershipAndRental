package com.lindstrom.service;

import com.lindstrom.model.*;
import com.lindstrom.pricing.PricePolicy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentalService {
    private List<Rental> rentals = new ArrayList<>();

    public void rentItem(Member member, Item item, int days, PricePolicy pricePolicy) {
        Rental rental = new Rental(member, item, LocalDate.now(), LocalDate.now().plusDays(days));
        rentals.add(rental);
        item.setAvailable(false);
        System.out.println(item.getBrand() + " uthyrd till " + member.getName());
    }
    public List<Rental> getCurrentRentals() {
        return rentals;
    }
}
