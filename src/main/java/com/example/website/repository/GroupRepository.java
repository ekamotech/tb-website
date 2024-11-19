package com.example.website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.website.entity.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {
      
    // public Group findByIdAndCreatedBy(Long id, Long createdBy);
    
    public List<Group> findByCreatedByOrderByUpdatedAtDesc(Long createdBy);
}
