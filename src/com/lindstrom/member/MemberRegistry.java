package com.lindstrom.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberRegistry {
    public Member getMember(int memberId) {
        return members.get(memberId);
    }

    public enum StatusLevel {
        STANDARD,
        JUNIOR,

    }

    private final Map<Integer, Member> members = new HashMap<>();
    private int nextId = 1;

    public Member createAndAddMember(String name, StatusLevel statusLevel) {
        int id = nextId++;
        Member member = new Member(id, name, statusLevel);
        members.put(id, member);
        return member;
    }

    public boolean removeMember(int memberId) {
        return members.remove(memberId) != null;
    }

    public List<Member> listMembers() {
        return new ArrayList<>(members.values());
    }

    public List<Member> listByStatus(StatusLevel statusLevel) {
        List<Member> result = new ArrayList<>();
        for (Member m : members.values()) {
            if (m.getStatusLevel() == statusLevel) {
                result.add(m);
            }
        }
        return result;
    }

    public List<Member> searchByName(String searchTerm) {
        List<Member> result = new ArrayList<>();
        String lowerTerm = searchTerm.toLowerCase();
        for (Member member : members.values()) {
            if (member.getName().toLowerCase().contains(lowerTerm)) {
                result.add(member);
            }
        }
        return result;
    }

    public List<Member> filterByStatus(StatusLevel statusLevel) {
        return listByStatus(statusLevel);
    }


}
