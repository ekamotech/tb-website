package com.example.website.service;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.website.entity.Event;
import com.example.website.entity.Favorite;
import com.example.website.entity.User;
import com.example.website.entity.UserInf;
import com.example.website.form.EventForm;
import com.example.website.repository.EventRepository;
import com.example.website.repository.FavoriteRepository;
import com.example.website.repository.UserRepository;

@Service
public class FavoriteService {
    
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EventService eventService;
    private final FavoriteRepository favoriteRepository;
    
    public FavoriteService(UserRepository userRepository, EventRepository eventRepository, EventService eventService, FavoriteRepository favoriteRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.eventService = eventService;
        this.favoriteRepository = favoriteRepository;
    }
    
    public List<EventForm> index(Principal principal) throws IOException {
        Authentication authentication = (Authentication) principal;
        UserInf userInf = (UserInf) authentication.getPrincipal();
        
        List<Favorite> events = favoriteRepository.findByUserIdOrderByUpdatedAtDesc(userInf.getUserId());
        List<EventForm> list = new ArrayList<>();
        for (Favorite entity : events) {
            Event eventEntity = entity.getEvent();
            EventForm form = eventService.getEvent(userInf, eventEntity);
            list.add(form);
        }
        return list;
    }
    
    @Transactional
    public void createFavorite(Long userId, Long eventId) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("指定されたイベントは見つかりませんでした"));
        
        List<Favorite> results = favoriteRepository.findByUserIdAndEventId(userId, eventId);
        if (results.size() == 0) {
            Favorite favorite = new Favorite();
            favorite.setUser(user);
            favorite.setEvent(event);
            favoriteRepository.saveAndFlush(favorite);
        }

    }
    
    @Transactional
    public void destroyFavorite(Long userId, Long eventId) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("指定されたイベントは見つかりませんでした"));
        
        List<Favorite> results = favoriteRepository.findByUserIdAndEventId(userId, eventId);
        if (results.size() == 1) {
            favoriteRepository.deleteByUserIdAndEventId(userId, eventId);
        }
    }
    
    
    
    

}
