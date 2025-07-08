package com.pawel.musicshop.repository;

import com.pawel.musicshop.model.MusicCD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicCDRepository extends JpaRepository<MusicCD, String> {
    @Query("SELECT m FROM MusicCD m WHERE m.isActive = TRUE")
    List<MusicCD> findByIsActiveTrue();
}
