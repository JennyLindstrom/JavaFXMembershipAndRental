package com.lindstrom.rental;

import com.lindstrom.item.Inventory;
import com.lindstrom.item.Item;
import com.lindstrom.member.Member;
import com.lindstrom.member.MemberRegistry;
import com.lindstrom.pricing.JuniorPricePolicy;
import com.lindstrom.pricing.PricePolicy;
import com.lindstrom.pricing.StandardPricePolicy;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class RentalService {
    private final Inventory inventory;
    private final MemberRegistry memberRegistry;
    private final List<Rental> activeRentals;
    private PricePolicy pricePolicy;

    public RentalService(Inventory inventory, MemberRegistry memberRegistry) {
        this.inventory = inventory;
        this.memberRegistry = memberRegistry;
        this.activeRentals = new ArrayList<>();
        this.pricePolicy = new StandardPricePolicy();
    }

    public void setPricePolicy(PricePolicy pricePolicy) {
        this.pricePolicy = pricePolicy;
    }

    public Rental rentItem(int memberId, String itemId, LocalDate startDate, LocalDate endDate) {
        Member member = memberRegistry.getMember(memberId);
        Item item = inventory.getItemById(itemId);

        if (member == null || item == null || !item.isAvailable()) {
            return null;
        }
        int days = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
        if (days <= 0) return null;

        PricePolicy policyToUse = member.getStatusLevel() == MemberRegistry.StatusLevel.JUNIOR
                ? new JuniorPricePolicy() : this.pricePolicy;

        double price = policyToUse.calculatePrice(item, days);
        Rental rental = new Rental(member, item, startDate, endDate, price);

        activeRentals.add(rental);
        item.setAvailable(false);
        member.addRentalToHistory(rental);

        return rental;
    }

    public double endRental(Rental rental, LocalDate returnDate) {
        Item item = rental.getItem();
        Member member = rental.getMember();

        rental.setEndDate(returnDate);
        int days = (int) ChronoUnit.DAYS.between(rental.getStartDate(), returnDate) + 1;
        days = Math.max(days, 1);

        PricePolicy policyToUse = member.getStatusLevel() == MemberRegistry.StatusLevel.JUNIOR
                ? new JuniorPricePolicy() : this.pricePolicy;

        double realPrice = policyToUse.calculatePrice(item, days);
        rental.setRentalPrice(realPrice);

        item.setAvailable(true);
        activeRentals.remove(rental);

        return realPrice;
    }

    public List<Rental> getActiveRentals() {
        return new ArrayList<>(activeRentals);
    }

    public double getTotalRevenue() {
        double sum = 0;
        for (Member member : memberRegistry.listMembers()) {
            for (Rental rental : member.getRentalHistory()) {
                sum += rental.getRentalPrice();
            }
        }
        return sum;
    }

}
