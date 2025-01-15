package com.example.website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.website.entity.Group;
import com.example.website.entity.GroupMember;
import com.example.website.entity.GroupMember.Authority;
import com.example.website.entity.User;

/**
 * グループメンバーエンティティのためのリポジトリインターフェース。
 * グループメンバーエンティティに対するCRUD操作を提供します。
 */
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    
    /**
     * 指定されたユーザーと権限に基づいてグループメンバーを取得し、更新日時の降順で並べ替えます。
     *
     * @param user 検索対象のユーザー
     * @param authority 検索対象の権限
     * @return 指定されたユーザーと権限に基づいて取得されたグループメンバーのリスト
     */
    List<GroupMember> findByUserAndAuthorityOrderByUpdatedAtDesc(User user, Authority authority);
    
    /**
     * 指定されたユーザーが特定のグループの管理者であるかを判定します。
     *
     * @param group 検索対象のグループ
     * @param user 検索対象のユーザー
     * @return ユーザーがグループの管理者である場合は true、それ以外の場合は false
     */
    @Query("SELECT COUNT(gm) > 0 " +
            "FROM GroupMember gm " +
            "WHERE gm.group = :group AND gm.user = :user AND gm.authority = 'ROLE_ADMIN'")
    boolean isUserGroupAdmin(@Param("group") Group group, @Param("user") User user);
}
