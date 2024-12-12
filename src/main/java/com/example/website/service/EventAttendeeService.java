package com.example.website.service;

import org.springframework.stereotype.Service;

import com.example.website.entity.Event;
import com.example.website.entity.EventAttendee;
import com.example.website.entity.EventAttendee.ParticipationStatus;
import com.example.website.entity.User;
import com.example.website.repository.EventAttendeeRepository;
import com.example.website.repository.EventRepository;
import com.example.website.repository.UserRepository;

@Service
public class EventAttendeeService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EventAttendeeRepository eventAttendeeRepository;

    public EventAttendeeService(UserRepository userRepository, EventRepository eventRepository, EventAttendeeRepository eventAttendeeRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.eventAttendeeRepository = eventAttendeeRepository;
    }

    public boolean isUserParticipating(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("指定されたイベントは見つかりませんでした"));
        ParticipationStatus status = EventAttendee.ParticipationStatus.PARTICIPATING;
        
        return eventAttendeeRepository.existsByEventAndUserAndParticipationStatus(event, user, status);
    }
}
