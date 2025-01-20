package com.example.website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.website.entity.EventAttendee;

/**
 * イベント参加者エンティティのためのリポジトリインターフェース。
 * イベント参加者エンティティに対するCRUD操作を提供します。
 */
public interface EventAttendeeRepository extends JpaRepository<EventAttendee, Long> {
    
    /**
     * 指定されたユーザーが特定のイベントに参加済みかを判定します。
     *
     * @param userId 検索対象のユーザーID
     * @param eventId 検索対象のイベントID
     * @return ユーザーが特定のイベントに参加済みの場合は true、それ以外の場合は false
     */
    @Query("SELECT COUNT(ea) > 0 " +
            "FROM EventAttendee ea " +
            "WHERE ea.user.userId = :userId AND ea.event.id = :eventId AND ea.participationStatus = 'PARTICIPATING'")
     boolean existsByUserIdAndEventIdAndParticipationStatus(@Param("userId") Long userId,
                                                            @Param("eventId") Long eventId);
    
    /**
     * 特定のイベントに参加済みのメンバーを取得します。
     *
     * @param eventId 検索対象のイベントID
     * @return 特定のイベントに参加済みのメンバーのリスト
     */
    @Query("SELECT ea " +
            "FROM EventAttendee ea " +
            "WHERE ea.event.id = :eventId AND ea.participationStatus = 'PARTICIPATING'")
     List<EventAttendee> findByEventIdAndParticipationStatus(@Param("eventId") Long eventId);

}
