package com.example.website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.website.entity.Event;
import com.example.website.entity.Group;

public interface EventRepository extends JpaRepository<Event, Long> {
    
    List<Event> findAllByOrderByUpdatedAtDesc();

    List<Event> findByGroupOrderByUpdatedAtDesc(Group group);
    
}
