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

/**
 * お気に入りに関連するサービスクラス。
 * お気に入りの作成、削除、取得などの操作を提供します。
 */
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
    
    /**
     * 認証されたユーザーのお気に入りイベント一覧を取得します。
     *
     * @param principal 認証されたユーザー情報
     * @return イベントフォームのリスト
     * @throws IOException 入出力例外が発生した場合
     */
    public List<EventForm> index(Principal principal) throws IOException {
        Authentication authentication = (Authentication) principal;
        UserInf userInf = (UserInf) authentication.getPrincipal();
        
        List<Favorite> events = favoriteRepository.findByUserIdOrderByUpdatedAtDesc(userInf.getUserId());
        List<EventForm> list = new ArrayList<>();
        for (Favorite entity : events) {
            EventForm form = eventService.getEvent(userInf.getUserId(), entity.getEvent().getId());
            list.add(form);
        }
        return list;
    }
    
    /**
     * イベントをお気に入りに登録します。
     *
     * @param userId ユーザーID
     * @param eventId イベントID
     * @throws IOException 入出力例外が発生した場合
     */
    @Transactional
    public void createFavorite(Long userId, Long eventId) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("指定されたイベントは見つかりませんでした"));
        
        List<Favorite> results = favoriteRepository.findByUserIdAndEventId(userId, eventId);
        if (results.size() == 0) {
            Favorite favorite = new Favorite();
            favorite.setUser(user);
            favorite.setEvent(event);
            favoriteRepository.saveAndFlush(favorite);
        }

    }
    
    /**
     * イベントのお気に入りを解除します。
     *
     * @param userId ユーザーID
     * @param eventId イベントID
     * @throws IOException 入出力例外が発生した場合
     */
    @Transactional
    public void destroyFavorite(Long userId, Long eventId) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("指定されたイベントは見つかりませんでした"));
        
        List<Favorite> results = favoriteRepository.findByUserIdAndEventId(userId, eventId);
        if (results.size() == 1) {
            favoriteRepository.deleteByUserIdAndEventId(userId, eventId);
        }
    }
    
    
    
    

}
