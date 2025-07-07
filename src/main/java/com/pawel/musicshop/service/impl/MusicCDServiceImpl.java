package com.pawel.musicshop.service.impl;

import com.pawel.musicshop.model.CartItem;
import com.pawel.musicshop.model.MusicCD;
import com.pawel.musicshop.repository.CartItemRepository;
import com.pawel.musicshop.repository.MusicCDRepository;
import com.pawel.musicshop.service.MusicCDService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MusicCDServiceImpl implements MusicCDService {
    private final MusicCDRepository musicCDRepository;
    private final CartItemRepository cartItemRepository;
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
    public List<MusicCD> findAllInCarts() {
        List<MusicCD> musicCDs = new ArrayList<>();
        List<CartItem> cartItems = cartItemRepository.findAll();
        for(CartItem cartItem : cartItems){
            musicCDs.add(cartItem.getCd());
        }
        return musicCDs;
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
        }else{
            Optional<MusicCD> optionalMusicCD = musicCDRepository.findById(musicCD.getId());
            if(optionalMusicCD.isPresent()){
                optionalMusicCD.get().setQuantity(optionalMusicCD.get().getQuantity() + musicCD.getQuantity());
                return musicCDRepository.save(optionalMusicCD.get());
            }
        }
        return musicCDRepository.save(musicCD);
    }

    @Override
    public List<MusicCD> findAvailableCDs() {
        List<MusicCD> availableMusicCDs = new ArrayList<>();
        List<MusicCD> activeMusicCDs = musicCDRepository.findByIsActiveTrue();
        for(MusicCD cd : activeMusicCDs){
            if(cd.getQuantity() > 0){
                availableMusicCDs.add(cd);
            }
        }
        return availableMusicCDs;
    }

    @Override
    public boolean isAvailable(String cdId) {
        List<MusicCD> availableMusicCDs = findAvailableCDs();
        return availableMusicCDs.stream().anyMatch(cd -> cd.getId().equals(cdId));
    }

    @Override
    @Transactional
    public boolean deleteById(String id) {
        Optional<MusicCD> musicCD = musicCDRepository.findById(id);
        if(musicCD.isPresent()){
            musicCD.get().setActive(false);
            musicCDRepository.save(musicCD.get());
            return true;
        }
        return false;
    }
}
