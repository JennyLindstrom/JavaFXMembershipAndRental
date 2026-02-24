package com.lindstrom.repository;

import com.lindstrom.model.Member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberRegistry {
    private List<Member> members = new ArrayList<>();

    public void addMember(Member member) {members.add(member);}
    public List<Member> getAllMembers() {return members;}


}
