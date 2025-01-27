package com.example.website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.website.entity.GroupMember;

/**
 * グループメンバーエンティティのためのリポジトリインターフェース。
 * グループメンバーエンティティに対するCRUD操作を提供します。
 */
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    
    /**
     * 指定されたユーザーが管理者であるグループメンバーを取得し、更新日時の降順で並べ替えます。
     *
     * @param userId 検索対象のユーザーID
     * @return 指定されたユーザーIDと権限に基づいて取得されたグループメンバーのリスト
     */
    @Query("SELECT gm " +
            "FROM GroupMember gm " +
            "WHERE gm.user.userId = :userId AND gm.authority = 'ROLE_ADMIN'" +
            "ORDER BY gm.updatedAt DESC")
    List<GroupMember> findByUserIdAndAuthorityOrderByUpdatedAtDesc(@Param("userId") Long userId);
    
    /**
     * 指定されたユーザーが特定のグループの管理者であるかを判定します。
     *
     * @param userId 検索対象のユーザーID
     * @param groupId 検索対象のグループID
     * @return ユーザーがグループの管理者である場合は true、それ以外の場合は false
     */
    @Query("SELECT COUNT(gm) > 0 " +
            "FROM GroupMember gm " +
            "WHERE gm.user.userId = :userId AND gm.group.id = :groupId AND gm.authority = 'ROLE_ADMIN'")
    boolean isUserGroupAdmin(@Param("userId") Long userId, @Param("groupId") Long groupId);
    
    /**
     * 指定されたユーザーが特定のグループに参加しているかを判定します。
     *
     * @param userId ユーザーID
     * @param groupId グループID
     * @return ユーザーがグループに参加している場合は true、それ以外の場合は false
     */
    @Query("SELECT COUNT(gm) > 0 " +
            "FROM GroupMember gm " +
            "WHERE gm.user.userId = :userId AND gm.group.id = :groupId")
    boolean isUserGroupMember(@Param("userId") Long userId, @Param("groupId") Long groupId);
    
    
    
    
}
