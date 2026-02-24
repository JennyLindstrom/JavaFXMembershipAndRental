package com.lindstrom.service;

import com.lindstrom.model.Member;
import com.lindstrom.repository.MemberRegistry;

public class MembershipService {
    private MemberRegistry memberRegistry;

    public MembershipService(MemberRegistry memberRegistry) {
        this.memberRegistry = memberRegistry;
    }

    //Lägga till medlem
    public void addNewMember(String id, String name, String level) {
        Member member = new Member (name, level);
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
}