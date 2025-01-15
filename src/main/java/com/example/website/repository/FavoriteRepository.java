package com.example.website.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.website.entity.Favorite;

/**
 * お気に入りエンティティのためのリポジトリインターフェース。
 * お気に入りエンティティに対するCRUD操作を提供します。
 */
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    
    /**
     * 指定されたIDのお気に入りを取得します。
     *
     * @param id 検索対象のお気に入りのID
     * @return 指定されたIDのお気に入りのオプショナル
     */
    public Optional<Favorite> findById(Long id);

    /**
     * 指定されたユーザーIDに基づいて、お気に入りを更新日時の降順で取得します。
     *
     * @param userId 検索対象のユーザーID
     * @return 指定されたユーザーIDに基づいて取得したお気に入りのリスト
     */
    @Query("SELECT f FROM Favorite f WHERE f.user.userId = :userId ORDER BY f.updatedAt DESC")
    List<Favorite> findByUserIdOrderByUpdatedAtDesc(@Param("userId") Long userId);

    /**
     * 指定されたユーザーIDとイベントIDに基づいて、お気に入りを取得します。
     *
     * @param userId 検索対象のユーザーID
     * @param eventId 検索対象のイベントID
     * @return 指定されたユーザーIDとイベントIDに基づいて取得したお気に入りのリスト
     */
    @Query("SELECT f FROM Favorite f WHERE f.user.userId = :userId AND f.event.id = :eventId")
    List<Favorite> findByUserIdAndEventId(@Param("userId") Long userId, @Param("eventId") Long eventId);

    /**
     * 指定されたユーザーIDとイベントIDに基づいて、お気に入りを削除します。
     *
     * @param userId 削除対象のユーザーID
     * @param eventId 削除対象のイベントID
     */
    @Modifying
    @Query("DELETE FROM Favorite f WHERE f.user.userId = :userId AND f.event.id = :eventId")
    void deleteByUserIdAndEventId(@Param("userId") Long userId, @Param("eventId") Long eventId);
}
