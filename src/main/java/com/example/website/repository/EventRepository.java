package com.example.website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.website.entity.Event;
import com.example.website.entity.Group;

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
     * @param group 検索対象のグループ
     * @return 指定されたグループに属するイベントのリスト
     */
    List<Event> findByGroupOrderByUpdatedAtDesc(Group group);
    
}
