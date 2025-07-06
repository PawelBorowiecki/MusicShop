package com.pawel.musicshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MusicShopApplication {

    public static void main(String[] args) {
        //TODO Zmiana isInCart na obiekt klasy Cart
        //TODO Dopracowac relacje w bazie danych
        SpringApplication.run(MusicShopApplication.class, args);
    }

}
