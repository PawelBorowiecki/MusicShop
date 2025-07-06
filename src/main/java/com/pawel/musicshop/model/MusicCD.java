package com.pawel.musicshop.model;

import jakarta.persistence.*;
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
    @Column(nullable = false)
    private String name;
    private String musicType;

    @Column(columnDefinition = "NUMERIC")
    private int releasedYear;

    @Column(columnDefinition = "NUMERIC")
    private double price;

    @Column(columnDefinition = "NUMERIC")
    private int quantity;

    @Column(columnDefinition = "BOOLEAN")
    private boolean isActive;
}
