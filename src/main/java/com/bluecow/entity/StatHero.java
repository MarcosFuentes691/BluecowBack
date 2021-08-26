package com.bluecow.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StatHero {
    private String name;
    private int gamesPlayed;
    private int mmr;

    public StatHero(String name) {
        this.name = name;
        this.gamesPlayed = 0;
        this.mmr = 0;
    }
}
