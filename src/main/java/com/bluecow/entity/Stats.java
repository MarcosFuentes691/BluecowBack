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
    private float avgMmrGain;
    private int bestMmr;
    private float avgMmr;
    private int worstMmr;
    private String bestHero;
    private String mostHero;
    private String worstHero;
    private int bestHeroNumber;
    private int mostHeroNumber;
    private int worstHeroNumber;
    private String time;//today,week,month,always
    private int[] positions;
    private int startMmr;
    private int endMmr;

    public Stats(String time) {
        this.time = time;
        this.gamesPlayed=0;
        this.avgMmrGain=0;
        this.bestMmr=-100000;
        this.worstMmr=100000;
        this.avgMmr=0;
        this.worstHero="none";
        this.mostHero="none";
        this.bestHero="none";
        this.worstHeroNumber=0;
        this.mostHeroNumber=0;
        this.positions= new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        this.bestHeroNumber=0;
        this.startMmr=0;
        this.endMmr=0;
    }

    public Stats(Stats prevStats) {
        this.time = prevStats.getTime();
        this.gamesPlayed=prevStats.getGamesPlayed();
        this.avgMmrGain=prevStats.getAvgMmrGain();
        this.bestMmr=prevStats.getBestMmr();
        this.worstMmr=prevStats.getWorstMmr();
        this.avgMmr=prevStats.getAvgMmr();
        this.worstHero=prevStats.getWorstHero();
        this.mostHero=prevStats.getMostHero();
        this.bestHero=prevStats.getBestHero();
        this.mostHeroNumber=prevStats.getMostHeroNumber();
        this.worstHeroNumber=prevStats.getWorstHeroNumber();
        this.bestHeroNumber=prevStats.getBestHeroNumber();
        this.positions= prevStats.getPositions();
        this.startMmr=prevStats.getStartMmr();
        this.startMmr=prevStats.getEndMmr();
    }
}
