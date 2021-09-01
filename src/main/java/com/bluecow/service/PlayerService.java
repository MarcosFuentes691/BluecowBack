package com.bluecow.service;

import com.bluecow.entity.*;
import com.bluecow.repository.GameRepository;
import com.bluecow.repository.PlayerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@Slf4j
public class PlayerService {

    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    GameRepository gameRepository;

    public Optional<Player> getByEmail(String email){
        return playerRepository.findByEmail(email);
    }

    public boolean existsEmail(String email){
        return playerRepository.existsByEmail(email);
    }

    public Player save(Player player){
        return playerRepository.save(player);
    }

    private final HashMap<String,StatHero> statHeroMap= new HashMap<>();

    public ArrayList<Stats> getPlayerStats(String email){
        ArrayList<Stats> statsArrayList = new ArrayList<>();
        Calendar now = Calendar.getInstance();
        Calendar past = Calendar.getInstance();
        past.add(Calendar.DATE,-1);
        statsArrayList.add(makeStats(email,now,past,"today",null));
        now.add(Calendar.DATE,-1);
        past.add(Calendar.DATE,-6);
        statsArrayList.add(makeStats(email,now,past, "last week",statsArrayList.get(0)));
        now.add(Calendar.DATE,-6);
        past.add(Calendar.DATE,-23);
        statsArrayList.add(makeStats(email,now,past, "last month",statsArrayList.get(statsArrayList.size()-1)));
        now.add(Calendar.DATE,-23);
        past.add(Calendar.DATE,-60);
        statsArrayList.add(makeStats(email,now,past, "last 3 months",statsArrayList.get(statsArrayList.size()-1)));
        now.add(Calendar.DATE,-60);
        statsArrayList.add(makeStats(email,now,playerRepository.findByEmail(email).get().getCreationDate(), "always",statsArrayList.get(statsArrayList.size()-1)));
        return statsArrayList;
    }

    public Stats makeStats(String email,Calendar from,Calendar to,String time,Stats prevStats) {
        Stats stat;
        if (prevStats == null)
            stat = new Stats(time);
        else
            stat = new Stats(prevStats);
        stat.setTime(time);
        from.add(Calendar.MINUTE, -1);
        to.add(Calendar.MINUTE, 1);
        List<Game> games = gameRepository.findGamesInPeriod(email, from, to, PageRequest.of(0, 50000)).getContent();
        for (int i = 0; i < games.size(); i++) {
            Game game = games.get(i);
            if (game.getMmr() > stat.getBestMmr())
                stat.setBestMmr(game.getMmr());
            if (game.getMmr() < stat.getWorstMmr())
                stat.setWorstMmr(game.getMmr());
            stat.setGamesPlayed(stat.getGamesPlayed() + 1);
            stat.setAvgMmr(stat.getAvgMmr() + game.getMmr());
            Integer prevMmr = 0;
            int mmrObtained = 0;
            mmrObtained += game.getDifference();
            stat.setAvgMmrGain(stat.getAvgMmrGain() + mmrObtained);
            statHeroMap.putIfAbsent(game.getHero(), new StatHero(game.getHero()));
            StatHero statHero = statHeroMap.get(game.getHero());
            statHero.setMmr(statHero.getMmr() + mmrObtained);
            statHero.setGamesPlayed(statHero.getGamesPlayed() + 1);
            statHeroMap.replace(game.getHero(), statHero);
        }
        if (statHeroMap.size() > 0){
            StatHero max = Collections.max(statHeroMap.values(),
                        (a, b) -> Float.compare(a.getMmr(), b.getMmr()));
            StatHero min = Collections.min(statHeroMap.values(),
                    (a, b) -> Float.compare(a.getMmr(), b.getMmr()));
            StatHero maxPlayed = Collections.max(statHeroMap.values(),
                    (a, b) -> Float.compare(a.getGamesPlayed(), b.getGamesPlayed()));
            stat.setAvgMmrGain(stat.getAvgMmrGain() / stat.getGamesPlayed());
            stat.setAvgMmr(stat.getAvgMmr() / stat.getGamesPlayed());
            stat.setBestHero(max.getName());
            stat.setBestHeroNumber(max.getMmr());
            stat.setWorstHero(min.getName());
            stat.setWorstHeroNumber(min.getMmr());
            stat.setMostHero(maxPlayed.getName());
            stat.setMostHeroNumber(maxPlayed.getGamesPlayed());
        }
        return stat;
    }
}
