package com.bluecow.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Hero {
    @Id
    @SequenceGenerator(
            name="gameid_sequence",
            sequenceName = "gameid_sequence",
            initialValue=195,
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="gameid_sequence")
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
    @Transient
    private String heroUrl;

    public Hero(String name) {
        this.name = name;
        this.avgPlace = (float) 0;
        this.mmr = 0;
        this.gamesPlayed =0 ;
        Calendar cal = Calendar.getInstance();
        cal.setTime(Date.from(Instant.EPOCH));
        this.lastUse = cal;
    }
}
