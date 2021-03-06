package com.bluecow.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    @Id
    @SequenceGenerator(
            name="gameid_sequence2",
            sequenceName = "gameid_sequence2",
            initialValue=1400,
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="gameid_sequence2")
    private Long id;
    @NotNull
    private String player;
    @NotNull
    private Integer place;
    @NotNull
    private Integer mmr;
    @NotNull
    private Calendar timestamp;//Timestamp timestamp;
    @NotNull
    private String hero;
    @Transient
    private String heroUrl;
    @Transient
    private String timestampString;
    private Integer difference;

}