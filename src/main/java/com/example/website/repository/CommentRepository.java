package com.example.website.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.website.entity.Comment;

/**
 * コメントエンティティのためのリポジトリインターフェース。
 * コメントエンティティに対するCRUD操作を提供します。
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
