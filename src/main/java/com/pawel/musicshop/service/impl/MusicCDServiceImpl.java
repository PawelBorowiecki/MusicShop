package com.pawel.musicshop.service.impl;

import com.pawel.musicshop.model.MusicCD;
import com.pawel.musicshop.repository.MusicCDRepository;
import com.pawel.musicshop.service.MusicCDService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MusicCDServiceImpl implements MusicCDService {
    private final MusicCDRepository musicCDRepository;
    @Override
    public List<MusicCD> findAll() {
        return musicCDRepository.findAll();
    }

    @Override
    public List<MusicCD> findAllActive() {
        return musicCDRepository.findByIsActiveTrue();
    }

    @Override
    public Optional<MusicCD> findById(String id) {
        return musicCDRepository.findById(id);
    }

    @Override
    public MusicCD save(MusicCD musicCD) {
        return musicCDRepository.save(musicCD);
    }

    @Override
    public List<MusicCD> findAvailableCDs() {
        return musicCDRepository.findByIsActiveTrueAndIsInCartFalse();
    }

    @Override
    public boolean isAvailable(String cdId) {
        return musicCDRepository.findByIdAndIsActiveTrueAndIsInCartFalse(cdId).isPresent();
    }

    @Override
    public void deleteById(String id) {
        musicCDRepository.deleteById(id);
    }
}
