package com.example.website.service;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.website.entity.Event;
import com.example.website.entity.Group;
import com.example.website.entity.User;
import com.example.website.entity.UserInf;
import com.example.website.form.EventForm;
import com.example.website.form.GroupForm;
import com.example.website.form.UserForm;
import com.example.website.repository.EventRepository;
import com.example.website.repository.GroupRepository;
import com.example.website.repository.UserRepository;

@Service
public class EventService {
    
    @Autowired
    private ModelMapper modelMapper;
    
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final EventRepository eventRepository;

    public EventService(UserRepository userRepository, GroupRepository groupRepository, EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.eventRepository = eventRepository;
    }
    
    public List<EventForm> index(Principal principal) throws IOException {
        Authentication authentication = (Authentication) principal;
        UserInf userInf = (UserInf) authentication.getPrincipal();
        
        List<Event> events = eventRepository.findAllByOrderByUpdatedAtDesc();
        List<EventForm> list = new ArrayList<>();
        for (Event entity : events) {
            EventForm form = getEvent(userInf, entity);
            list.add(form);
        }
        return list;
    }
    
    public EventForm getEvent(UserInf user, Event entity) throws IOException {
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        modelMapper.typeMap(Event.class, EventForm.class).addMappings(mapper -> mapper.skip(EventForm::setUser));
        modelMapper.typeMap(Event.class, EventForm.class).addMappings(mapper -> mapper.skip(EventForm::setGroup));
        
        EventForm form = modelMapper.map(entity, EventForm.class);
        
        UserForm userForm = modelMapper.map(entity.getUser(), UserForm.class);
        form.setUser(userForm);
        
        GroupForm groupForm = modelMapper.map(entity.getGroup(), GroupForm.class);
        form.setGroup(groupForm);
        
        return form;
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
