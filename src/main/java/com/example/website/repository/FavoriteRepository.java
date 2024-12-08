package com.example.website.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.website.entity.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    
    public Optional<Favorite> findById(Long id);

    @Query("SELECT f FROM Favorite f WHERE f.user.userId = :userId ORDER BY f.updatedAt DESC")
    List<Favorite> findByUserIdOrderByUpdatedAtDesc(@Param("userId") Long userId);

    @Query("SELECT f FROM Favorite f WHERE f.user.userId = :userId AND f.event.id = :eventId")
    List<Favorite> findByUserIdAndEventId(@Param("userId") Long userId, @Param("eventId") Long eventId);

    @Modifying
    @Query("DELETE FROM Favorite f WHERE f.user.userId = :userId AND f.event.id = :eventId")
    void deleteByUserIdAndEventId(@Param("userId") Long userId, @Param("eventId") Long eventId);
}
