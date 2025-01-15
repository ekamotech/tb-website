package com.example.website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.website.entity.Event;
import com.example.website.entity.EventAttendee;
import com.example.website.entity.EventAttendee.ParticipationStatus;
import com.example.website.entity.User;

/**
 * イベント参加者エンティティのためのリポジトリインターフェース。
 * イベント参加者エンティティに対するCRUD操作を提供します。
 */
public interface EventAttendeeRepository extends JpaRepository<EventAttendee, Long> {
    
    /**
     * 指定されたユーザーが特定のイベントに参加済みかを判定します。
     *
     * @param event 検索対象のイベント
     * @param user 検索対象のユーザー
     * @param status 参加ステータス
     * @return ユーザーが特定のイベントに参加済みの場合は true、それ以外の場合は false
     */
    boolean existsByEventAndUserAndParticipationStatus(Event event, User user, ParticipationStatus status);
    
    /**
     * 特定のイベントに参加済みのメンバーを取得します。
     *
     * @param event 検索対象のイベント
     * @param status 参加ステータス
     * @return 特定のイベントに参加済みのメンバーのリスト
     */
    List<EventAttendee> findByEventAndParticipationStatus(Event event, ParticipationStatus status);

}
