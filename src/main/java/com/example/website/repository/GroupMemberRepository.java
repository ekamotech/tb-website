package com.example.website.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.website.entity.GroupMember;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

}
