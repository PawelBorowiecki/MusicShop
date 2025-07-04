package com.pawel.musicshop.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "music_cds")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MusicCD {
    @Id
    @Column(nullable = false, unique = true)
    private String id;
    private String name;
    private String musicType;

    @Column(columnDefinition = "NUMERIC")
    private int releasedYear;

    @Column(columnDefinition = "NUMERIC")
    private double price;

    @Column(columnDefinition = "BOOLEAN")
    private boolean isActive;

    @Column(columnDefinition = "BOOLEAN")
    private boolean isInCart;
}
