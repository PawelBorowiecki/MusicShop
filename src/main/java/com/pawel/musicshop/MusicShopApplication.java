package com.pawel.musicshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MusicShopApplication {

    public static void main(String[] args) {
        //TODO Zmniejszyc ilosc po zrobieniu zamowienia
        //TODO Usuwac z innych koszykow gdy nastapi zamowienie lub to jakos sprawdzac
        SpringApplication.run(MusicShopApplication.class, args);
    }

}
