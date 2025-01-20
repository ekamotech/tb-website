package com.example.website.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.website.entity.Group;

/**
 * グループエンティティのためのリポジトリインターフェース。
 * グループエンティティに対するCRUD操作を提供します。
 */
public interface GroupRepository extends JpaRepository<Group, Long> {
    
}
