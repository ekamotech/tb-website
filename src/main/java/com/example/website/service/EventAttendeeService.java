package com.example.website.service;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

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
    
    @Transactional
    public void saveAttendee(User user, Event event) {
        // 重複チェック
        boolean isAlreadyJoined = eventAttendeeRepository.existsByEventAndUserAndParticipationStatus(event, user, EventAttendee.ParticipationStatus.PARTICIPATING);
        if (isAlreadyJoined) {
            throw new IllegalArgumentException("すでに参加済みです");
        }

        // 参加登録
        EventAttendee eventAttendee = new EventAttendee();
        eventAttendee.setUser(user);
        eventAttendee.setEvent(event);
        eventAttendee.setParticipationStatus(EventAttendee.ParticipationStatus.PARTICIPATING);
        eventAttendeeRepository.save(eventAttendee);
    }
    
    public List<User> getAttendeesByEvent(Event event) {
        List<EventAttendee> attendees = eventAttendeeRepository.findByEventAndParticipationStatus(
            event,
            EventAttendee.ParticipationStatus.PARTICIPATING
        );

        List<User> users = attendees.stream()
                        .map(EventAttendee::getUser)
                        .collect(Collectors.toList());
        
        return users;
    }

}
