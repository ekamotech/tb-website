package com.example.website.service;

import java.io.IOException;

import jakarta.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.website.entity.Comment;
import com.example.website.entity.Event;
import com.example.website.entity.User;
import com.example.website.form.CommentForm;
import com.example.website.repository.CommentRepository;
import com.example.website.repository.EventRepository;
import com.example.website.repository.UserRepository;

@Service
public class CommentService {
    
    @Autowired
    private ModelMapper modelMapper;
    
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;
    
    public CommentService(UserRepository userRepository, EventRepository eventRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.commentRepository = commentRepository;
    }
    
    public CommentForm createCommentForm(Long userId, Long eventId) {
        CommentForm form = new CommentForm();
        form.setUserId(userId);
        form.setEventId(eventId);
        return form;
    }
    
    @Transactional
    public void createComment(Long userId, Long eventId, CommentForm form) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("指定されたイベントは見つかりませんでした"));
        
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setEvent(event);
        comment.setDescription(form.getDescription());
        commentRepository.saveAndFlush(comment);
        
    }

}
