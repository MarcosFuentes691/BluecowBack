package com.bluecow.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    @Id
    @SequenceGenerator(
            name="gameid_sequence",
            sequenceName = "gameid_sequence",
            initialValue=195,
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="gameid_sequence")
    //@GeneratedValue(strategy = GenerationType.TABLE)//GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String player;
    @NotNull
    private Integer place;
    @NotNull
    private Integer mmr;
    @NotNull
    private Timestamp timestamp;
    @NotNull
    private String hero;
    @Transient
    private String heroUrl;

}