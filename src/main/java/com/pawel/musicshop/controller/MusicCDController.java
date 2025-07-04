package com.pawel.musicshop.controller;

import com.pawel.musicshop.service.MusicCDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/musicCD")
public class MusicCDController {
    private final MusicCDService musicCDService;

    @Autowired
    public MusicCDController(MusicCDService musicCDService) {
        this.musicCDService = musicCDService;
    }
}
