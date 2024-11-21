package com.example.website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.website.entity.GroupMember;
import com.example.website.entity.GroupMember.Authority;
import com.example.website.entity.User;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    List<GroupMember> findByUserAndAuthority(User user, Authority authority);
}
