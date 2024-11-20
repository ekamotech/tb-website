package com.example.website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.website.entity.GroupMember;
import com.example.website.entity.GroupMember.Authority;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    List<GroupMember> findByUserIdAndAuthority(Long userId, Authority authority);
}
