package com.example.website.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.website.entity.EventAttendee;

public interface EventAttendeeRepository extends JpaRepository<EventAttendee, Long> {

}
