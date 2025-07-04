package com.pawel.musicshop.service;

import com.pawel.musicshop.model.MusicCD;

import java.util.List;
import java.util.Optional;

public interface MusicCDService {
    List<MusicCD> findAll();
    List<MusicCD> findAllActive();
    Optional<MusicCD> findById(String id);
    MusicCD save(MusicCD musicCD);
    List<MusicCD> findAvailableCDs();
    boolean isAvailable(String cdId);
    void deleteById(String id);
}
