package com.pawel.musicshop.repository;

import com.pawel.musicshop.model.MusicCD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface MusicCDRepository extends JpaRepository<MusicCD, String> {
    @Query("SELECT m FROM MusicCD m WHERE m.isActive = TRUE")
    List<MusicCD> findByIsActiveTrue();
    @Query("SELECT m FROM MusicCD m WHERE m.isActive = TRUE AND m.isInCart = FALSE")
    List<MusicCD> findByIsActiveTrueAndIsInCartFalse();
    @Query("SELECT m FROM MusicCD m WHERE m.id = ?1 AND m.isActive = TRUE")
    Optional<MusicCD> findByIdAndIsActiveTrue(String id);
    @Query("SELECT m FROM MusicCD m WHERE m.id = ?1 AND m.isActive = TRUE AND m.isInCart = FALSE")
    Optional<MusicCD> findByIdAndIsActiveTrueAndIsInCartFalse(String id);
}
