package com.example.website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.website.entity.Event;

/**
 * イベントエンティティのためのリポジトリインターフェース。
 * イベントエンティティに対するCRUD操作を提供します。
 */
public interface EventRepository extends JpaRepository<Event, Long> {
    
    /**
     * 更新日時の降順で全てのイベントを取得します。
     *
     * @return 更新日時の降順で取得されたイベントのリスト
     */
    List<Event> findAllByOrderByUpdatedAtDesc();

    /**
     * 指定されたグループに属するイベントを、更新日時の降順で取得します。
     *
     * @param groupId 検索対象のグループID
     * @return 指定されたグループに属するイベントのリスト
     */
    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE e.group.id = :groupId " +
            "ORDER BY e.updatedAt DESC")
     List<Event> findByGroupIdOrderByUpdatedAtDesc(@Param("groupId") Long groupId);
}
