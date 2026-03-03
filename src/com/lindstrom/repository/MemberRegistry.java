package com.lindstrom.repository;

import com.lindstrom.model.Member;

import java.util.ArrayList;
import java.util.List;

public class MemberRegistry {
    private final List<Member> members = new ArrayList<>();

    public void addMember(Member member) {
        members.add(member);
    }

    public List<Member> getAllMembers() {
        return members;
    }


}
