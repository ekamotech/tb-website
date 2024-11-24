package com.example.website.service;

import java.io.IOException;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.website.entity.Event;
import com.example.website.entity.Group;
import com.example.website.entity.User;
import com.example.website.form.EventForm;
import com.example.website.repository.EventRepository;
import com.example.website.repository.GroupRepository;
import com.example.website.repository.UserRepository;

@Service
public class EventService {
    
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final EventRepository eventRepository;

    public EventService(UserRepository userRepository, GroupRepository groupRepository, EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.eventRepository = eventRepository;
    }
    
    public EventForm createEventForm(Long userId, Long groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("指定されたグループは見つかりませんでした"));

        EventForm form = new EventForm();
        form.setUserId(userId);
        form.setGroupId(group.getId());
        return form;
    }
    
    @Transactional
    public void createEvent(Long userId, Long groupId, EventForm form) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("指定されたグループは見つかりませんでした"));
        
        Event event = new Event();
        event.setUser(user);
        event.setGroup(group);
        event.setTitle(form.getTitle());
        event.setDescription(form.getDescription());
        event.setDate(form.getDate());
        event.setStartTime(form.getStartTime());
        event.setEndTime(form.getEndTime());
        eventRepository.saveAndFlush(event);
        
    }
}
