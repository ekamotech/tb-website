package com.example.website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.website.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    
    List<Event> findAllByOrderByUpdatedAtDesc();
    

}
