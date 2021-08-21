package com.bluecow.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;


@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Hero {
    private Float avgPlace;
    private Integer mmr;
    private Timestamp lastUse;
    private String name;
    private String heroUrl;
    private Integer gamesPlayed;
    public Hero(String name) {
        this.name = name;
    }
}
