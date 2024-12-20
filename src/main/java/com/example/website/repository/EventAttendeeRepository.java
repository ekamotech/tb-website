package com.example.website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.website.entity.Event;
import com.example.website.entity.EventAttendee;
import com.example.website.entity.EventAttendee.ParticipationStatus;
import com.example.website.entity.User;

public interface EventAttendeeRepository extends JpaRepository<EventAttendee, Long> {
    
    // 指定されたユーザーが特定のイベントに参加済みかを判定
    boolean existsByEventAndUserAndParticipationStatus(Event event, User user, ParticipationStatus status);
    
    // 特定のイベントに参加済みのメンバーを取得
    List<EventAttendee> findByEventAndParticipationStatus(Event event, ParticipationStatus status);

}
