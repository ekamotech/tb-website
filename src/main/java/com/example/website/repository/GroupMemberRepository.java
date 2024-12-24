package com.example.website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.website.entity.Group;
import com.example.website.entity.GroupMember;
import com.example.website.entity.GroupMember.Authority;
import com.example.website.entity.User;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    
    List<GroupMember> findByUserAndAuthorityOrderByUpdatedAtDesc(User user, Authority authority);
    
    // 指定されたユーザーが特定のグループの管理者であるかを判定
    @Query("SELECT COUNT(gm) > 0 " +
            "FROM GroupMember gm " +
            "WHERE gm.group = :group AND gm.user = :user AND gm.authority = 'ROLE_ADMIN'")
    boolean isUserGroupAdmin(@Param("group") Group group, @Param("user") User user);
}
