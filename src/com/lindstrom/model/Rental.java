package com.lindstrom.model;

import com.lindstrom.pricing.PricePolicy;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Rental {
    private final Member member;
    private final Item item;
    private final LocalDate startDate;
    private LocalDate endDate;


    public Rental(Member member, Item item, LocalDate startDate, LocalDate endDate) {
        this.member = member;
        this.item = item;
        this.startDate = startDate;
        this.endDate = endDate;

    }

    public long getDurationDays() {
        return ChronoUnit.DAYS.between(startDate, endDate);

    }

    public double getTotalCost(PricePolicy pricePolicy) {
        return item.getRentalCost((int) getDurationDays(), pricePolicy);
    }
    public Member getMember() {
        return member;

    }

    public Item getItem() {
        return item;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }


    @Override
    public String toString() {
        return "Uthyrning: " + item + " till " + member +
                ", Från: " + startDate + " Till: " + endDate;
    }
}
