package com.bluecow.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Hero {
    private Long id;
    @NotNull
    private String player;
    @NotNull
    private Float avgPlace;
    @NotNull
    private Integer mmr;
    @NotNull
    private Calendar lastUse;
    @NotNull
    private String name;
    @NotNull
    private Integer gamesPlayed;

    private int[] positions;

    public Hero(String name) {
        this.name = name;
        this.avgPlace = (float) 0;
        this.mmr = 0;
        this.gamesPlayed =0 ;
        Calendar cal = Calendar.getInstance();
        cal.setTime(Date.from(Instant.EPOCH));
        this.lastUse = cal;
        this.positions= new int[]{0, 0, 0, 0, 0, 0, 0, 0};
    }
}
