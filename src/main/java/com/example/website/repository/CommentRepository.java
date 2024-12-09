package com.example.website.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.website.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
