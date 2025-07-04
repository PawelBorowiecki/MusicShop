package com.pawel.musicshop.service.impl;

import com.pawel.musicshop.model.MusicCD;
import com.pawel.musicshop.service.MusicCDService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MusicCDServiceImpl implements MusicCDService {
    @Override
    public List<MusicCD> findAll() {
        return null;
    }

    @Override
    public List<MusicCD> findAllActive() {
        return null;
    }

    @Override
    public Optional<MusicCD> findById(String id) {
        return Optional.empty();
    }

    @Override
    public MusicCD save(MusicCD MusicCD) {
        return null;
    }

    @Override
    public List<MusicCD> findAvailableCDs() {
        return null;
    }

    @Override
    public boolean isAvailable(String cdId) {
        return false;
    }

    @Override
    public void deleteById(String id) {

    }
}
