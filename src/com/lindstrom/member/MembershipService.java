package com.lindstrom.member;

import java.util.List;

public class MembershipService {
    private final MemberRegistry memberRegistry;

    public MembershipService(MemberRegistry memberRegistry) {
        this.memberRegistry = memberRegistry;
    }

    public Member addMember(String name, String statusStr) {
        MemberRegistry.StatusLevel status = parseStatusLevel(statusStr);
        return memberRegistry.createAndAddMember(name, status);
    }

    public boolean removeMember(int memberId) {
        return memberRegistry.removeMember(memberId);
    }


    public List<Member> listAllMembers() {
        return memberRegistry.listMembers();
    }

    public List<Member> listByStatus(String statusStr) {
        MemberRegistry.StatusLevel status = parseStatusLevel(statusStr);
        return memberRegistry.listByStatus(status);
    }

    public List<Member> searchMembersByName(String searchTerm) {
        return memberRegistry.searchByName(searchTerm);
    }

    public List<Member> filterMembersByStatus(String statusStr) {
        MemberRegistry.StatusLevel status = parseStatusLevel(statusStr);
        return memberRegistry.filterByStatus(status);
    }

    private MemberRegistry.StatusLevel parseStatusLevel(String statusStr) {
        try {
            return MemberRegistry.StatusLevel.valueOf(statusStr.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return MemberRegistry.StatusLevel.STANDARD;
        }
    }

}
