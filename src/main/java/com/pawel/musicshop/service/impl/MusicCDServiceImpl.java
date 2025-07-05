package com.pawel.musicshop.service.impl;

import com.pawel.musicshop.model.MusicCD;
import com.pawel.musicshop.repository.MusicCDRepository;
import com.pawel.musicshop.service.MusicCDService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MusicCDServiceImpl implements MusicCDService {
    private final MusicCDRepository musicCDRepository;
    @Override
    @Transactional(readOnly = true)
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
    @Transactional
    public MusicCD save(MusicCD musicCD) {
        if(musicCD.getId() == null || musicCD.getId().isBlank()){
            musicCD.setId(UUID.randomUUID().toString());
            musicCD.setActive(true);
        }
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
    public boolean deleteById(String id) {
        Optional<MusicCD> musicCD = musicCDRepository.findById(id);
        if(musicCD.isPresent()){
            musicCD.get().setActive(false);
            return true;
        }
        return false;
    }
}
