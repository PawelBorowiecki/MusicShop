package com.pawel.musicshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MusicShopApplication {

    public static void main(String[] args) {
        //TODO Moze dodac mozliwosc sprawdzenia wartosci produktow w koszyku bez robienia zamowienia
        //TODO Dodac security
        //TODO Dodac platnosci
        //TODO Dodac mozliwosc dodawania plyt do koszyka
        //TODO Implementacja kontrolerow
        SpringApplication.run(MusicShopApplication.class, args);
    }

}
