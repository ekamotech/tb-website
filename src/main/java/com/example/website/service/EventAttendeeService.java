package com.example.website.service;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.website.entity.Event;
import com.example.website.entity.EventAttendee;
import com.example.website.entity.User;
import com.example.website.repository.EventAttendeeRepository;
import com.example.website.repository.EventRepository;
import com.example.website.repository.UserRepository;

/**
 * イベント参加者に関連するサービスクラス。
 * イベント参加者の管理や操作を提供します。
 */
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

    /**
     * 指定されたユーザーが特定のイベントに参加済みかを判定します。
     *
     * @param userId ユーザーID
     * @param eventId イベントID
     * @return ユーザーが特定のイベントに参加済みの場合は true、それ以外の場合は false
     */
    public boolean isUserParticipating(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("指定されたイベントは見つかりませんでした"));
        
        return eventAttendeeRepository.existsByUserIdAndEventIdAndParticipationStatus(userId, eventId);
    }
    
    /**
     * イベントに参加者を登録します。
     *
     * @param user ユーザーオブジェクト
     * @param event イベントオブジェクト
     */
    @Transactional
    public void saveAttendee(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("イベントが見つかりません"));
        
        // 重複チェック
        boolean isAlreadyJoined = eventAttendeeRepository.existsByUserIdAndEventIdAndParticipationStatus(userId, eventId);
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
    
    /**
     * 指定されたイベントの参加者一覧を取得します。
     *
     * @param event イベントオブジェクト
     * @return 参加者ユーザーオブジェクトのリスト
     */
    public List<User> getAttendeesByEvent(Long eventId) {
        
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("指定されたイベントは見つかりませんでした"));

        List<EventAttendee> attendees = eventAttendeeRepository.findByEventIdAndParticipationStatus(eventId);

        List<User> users = attendees.stream()
                        .map(EventAttendee::getUser)
                        .collect(Collectors.toList());
        
        return users;
    }

}
