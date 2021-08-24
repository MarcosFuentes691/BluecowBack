package com.bluecow.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;


@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Stats {
    private int gamesPlayed;
    private float gamesPerDay;
    private float avgMmrGain;
    private int bestMmr;
    private float avgMmr;
    private int worstMmr;
    private String bestHero;
    private String mostHero;
    private String worstHero;
    private String time;//today,week,month,always

    public Stats(String empty) {
        if(empty.equals("new stat")){
            this.gamesPerDay=0;
            this.gamesPlayed=0;
            this.avgMmr=0;
            this.avgMmrGain=0;
            this.bestMmr=-100000;
            this.worstMmr=100000;
            this.avgMmr=0;
            this.worstHero="none";
            this.mostHero="none";
            this.bestHero="none";
        }
    }
}
