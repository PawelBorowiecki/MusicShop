package com.pawel.musicshop.controller;

import com.pawel.musicshop.model.MusicCD;
import com.pawel.musicshop.service.MusicCDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/musicCD")
public class MusicCDController {
    private final MusicCDService musicCDService;

    @Autowired
    public MusicCDController(MusicCDService musicCDService) {
        this.musicCDService = musicCDService;
    }

    @GetMapping("/all")
    public List<MusicCD> getAllMusicCDs(){
        return musicCDService.findAll();
    }
    @GetMapping("/allActive")
    public List<MusicCD> getAllActiveMusicCDs(){
        return musicCDService.findAllActive();
    }

    @GetMapping("/allAvailable")
    public List<MusicCD> getAvailableMusicCDs(){
        return musicCDService.findAvailableCDs();
    }

    @GetMapping("/allInCarts")
    public List<MusicCD> getAllInCarts(){
        return musicCDService.findAllInCarts();
    }

    @GetMapping("/get={id}")
    public ResponseEntity<MusicCD> getMusicCDById(@PathVariable String id){
        return musicCDService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<MusicCD> addMusicCD(@RequestBody MusicCD musicCD){
        try{
            MusicCD savedMusicCD = musicCDService.save(musicCD);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedMusicCD);
        }catch(Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @DeleteMapping("/{id}")
    public void deleteMusicCD(@PathVariable String id){
        musicCDService.deleteById(id);
    }
}
