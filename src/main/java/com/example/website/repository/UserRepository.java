package com.example.website.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.website.entity.User;

/**
 * ユーザーエンティティのためのリポジトリインターフェース。
 * ユーザーエンティティに対するCRUD操作を提供します。
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * メールアドレスでユーザーを検索します。
     *
     * @param username 検索するメールアドレス
     * @return 指定されたメールアドレスを持つユーザー
     */
    User findByUsername(String username);
}
