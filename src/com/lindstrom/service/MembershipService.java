package com.lindstrom.service;

import com.lindstrom.model.Member;
import com.lindstrom.repository.MemberRegistry;

import java.io.*;

public class MembershipService {
    private final MemberRegistry memberRegistry;

    public MembershipService(MemberRegistry memberRegistry) {
        this.memberRegistry = memberRegistry;
    }

    //Lägga till medlem
    public void addNewMember(String id, String name, String level) {
        Member member = new Member(name, level);
        memberRegistry.addMember(member);
        System.out.println("Ny medlem skapad: " + member);
    }


    //Hitta medlem genom namn
    public Member findMemberByName(String firstName, String lastName) {
        return memberRegistry.getAllMembers().stream()
                .filter(member -> member.getName().equalsIgnoreCase(firstName))
                .findFirst()
                .orElse(null);

    }

    public void loadFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    memberRegistry.addMember(new Member(parts[0].trim(), parts[1].trim()));
                }
            }
        }
    }

    public void saveToFile(String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            memberRegistry.getAllMembers().forEach(member ->
                    writer.println(member.getName() + "," + member.getLevel()));
        }
    }
}