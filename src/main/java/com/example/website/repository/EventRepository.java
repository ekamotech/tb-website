package com.example.website.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.website.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    

}
