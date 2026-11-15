package com.pawel.musicshop.service;

import com.pawel.musicshop.model.MusicCD;
import com.pawel.musicshop.repository.CartItemRepository;
import com.pawel.musicshop.repository.MusicCDRepository;
import com.pawel.musicshop.service.impl.MusicCDServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MusicCDServiceImplTest {

    @Mock
    private MusicCDRepository musicCDRepository;
    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private MusicCDServiceImpl musicCDService;

    @Test
    void save_assignsIdAndActivatesNewCd() {
        MusicCD incoming = MusicCD.builder()
                .id(" ")
                .name("Blue Train")
                .quantity(4)
                .build();
        when(musicCDRepository.save(any(MusicCD.class))).thenAnswer(inv -> inv.getArgument(0));

        MusicCD saved = musicCDService.save(incoming);

        assertFalse(saved.getId().isBlank());
        assertTrue(saved.isActive());
        verify(musicCDRepository).save(incoming);
    }

    @Test
    void save_addsQuantityWhenIdAlreadyExists() {
        MusicCD stored = MusicCD.builder()
                .id("cd-1")
                .name("Blue Train")
                .quantity(2)
                .isActive(true)
                .build();
        MusicCD incoming = MusicCD.builder()
                .id("cd-1")
                .quantity(5)
                .build();
        when(musicCDRepository.findById("cd-1")).thenReturn(Optional.of(stored));
        when(musicCDRepository.save(stored)).thenReturn(stored);

        MusicCD saved = musicCDService.save(incoming);

        assertEquals(7, saved.getQuantity());
        verify(musicCDRepository).save(stored);
    }

    @Test
    void findAvailableCDs_keepsActiveWithPositiveStock() {
        MusicCD available = MusicCD.builder().id("a").quantity(2).isActive(true).build();
        MusicCD empty = MusicCD.builder().id("b").quantity(0).isActive(true).build();
        when(musicCDRepository.findByIsActiveTrue()).thenReturn(List.of(available, empty));

        List<MusicCD> result = musicCDService.findAvailableCDs();

        assertEquals(List.of(available), result);
    }

    @Test
    void deleteById_softDeletes() {
        MusicCD stored = MusicCD.builder()
                .id("cd-1")
                .isActive(true)
                .build();
        when(musicCDRepository.findById("cd-1")).thenReturn(Optional.of(stored));

        assertTrue(musicCDService.deleteById("cd-1"));

        ArgumentCaptor<MusicCD> captor = ArgumentCaptor.forClass(MusicCD.class);
        verify(musicCDRepository).save(captor.capture());
        assertFalse(captor.getValue().isActive());
        verify(musicCDRepository, never()).deleteById(any());
        verify(musicCDRepository, never()).delete(any());
    }

    @Test
    void deleteById_returnsFalseWhenMissing() {
        when(musicCDRepository.findById("missing")).thenReturn(Optional.empty());

        assertFalse(musicCDService.deleteById("missing"));
        verify(musicCDRepository, never()).save(any());
    }
}
