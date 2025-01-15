package com.example.website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.website.entity.Group;
import com.example.website.entity.User;

/**
 * グループエンティティのためのリポジトリインターフェース。
 * グループエンティティに対するCRUD操作を提供します。
 */
public interface GroupRepository extends JpaRepository<Group, Long> {
    
    /**
     * 指定されたユーザーによって作成されたグループを、更新日時の降順で取得します。
     *
     * @param createdBy グループを作成したユーザー
     * @return 指定されたユーザーによって作成されたグループのリスト
     */
    public List<Group> findByCreatedByOrderByUpdatedAtDesc(User createdBy);
}
