package com.lindstrom.model;


public class Member {
    private String memberId;
    private String name;
    private String level;


    public Member(String name, String level) {

     this.memberId = memberId;
     this.name = name;
     this.level = level;

    }
    //Getters

    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }
    public String getLevel() {return level;}
    public void setLevel(String level) {this.level = level;}

    @Override
    public String toString() {
        return "ID: " + memberId + ", Namn: " + getName() + ", Status: " + getLevel();
    }
}

