package com.example.website.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.website.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}
