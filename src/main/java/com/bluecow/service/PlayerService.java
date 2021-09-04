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

    private HashMap<String,StatHero> statHeroMap= new HashMap<>();

    public ArrayList<Stats> getPlayerStats(String email){
        statHeroMap=new HashMap<>();
        ArrayList<Stats> statsArrayList = new ArrayList<>();
        Calendar now = Calendar.getInstance();
        Calendar past = Calendar.getInstance();
        past.add(Calendar.DATE,-1);
        statsArrayList.add(makeStats(email,now,past,"Last 24 hr",null));
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
        past.add(Calendar.YEAR,-25);
        statsArrayList.add(makeStats(email,now,past, "always",statsArrayList.get(statsArrayList.size()-1)));
        return statsArrayList;
    }

    public Stats makeStats(String email,Calendar to,Calendar from,String time,Stats prevStats) {
        Stats stat;
        StatHero max;
        StatHero min;
        StatHero maxPlayed;
        if (prevStats == null)
            stat = new Stats(time);
        else
            stat = new Stats(prevStats);

        log.info(from.getTime().toString());
        log.info(to.getTime().toString());

        stat.setTime(time);
        int[] positions = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        Calendar endMmrStamp = null;
        List<Game> games = gameRepository.findGamesInPeriod(email, from, to, PageRequest.of(0, 50000)).getContent();
        for (int i = 0; i < games.size(); i++) {
            Game game = games.get(i);
            if (game.getMmr() > stat.getBestMmr())
                stat.setBestMmr(game.getMmr());
            if (game.getMmr() < stat.getWorstMmr())
                stat.setWorstMmr(game.getMmr());
            if(i==0){
                stat.setEndMmr(gameRepository.getFirstByTimestampIsLessThanAndPlayerOrderByTimestampDesc
                        (game.getTimestamp(),game.getPlayer()).getMmr());
                endMmrStamp=(gameRepository.getFirstByTimestampIsLessThanAndPlayerOrderByTimestampDesc
                        (game.getTimestamp(),game.getPlayer()).getTimestamp());
            }
            else if(endMmrStamp.before(game.getTimestamp())) {
                    stat.setEndMmr(game.getMmr());
                    endMmrStamp = game.getTimestamp();
                }
            if(i==games.size()-1) {
                try {
                    stat.setStartMmr(gameRepository.getFirstByTimestampIsLessThanAndPlayerOrderByTimestampDesc
                            (game.getTimestamp(), game.getPlayer()).getMmr());
                }
                catch (Exception e){
                    stat.setStartMmr(0);
                }
                if(stat.getStartMmr()>stat.getBestMmr())
                    stat.setBestMmr(stat.getStartMmr());
                if(stat.getStartMmr()<stat.getWorstMmr())
                    stat.setWorstMmr(stat.getStartMmr());
            }
            stat.setGamesPlayed(stat.getGamesPlayed() + 1);
            stat.setAvgMmr(stat.getAvgMmr() + game.getMmr());
            stat.setAvgMmrGain(stat.getAvgMmrGain() + game.getDifference());
            positions[games.get(i).getPlace()-1]++;
            statHeroMap.putIfAbsent(game.getHero(), new StatHero(game.getHero()));
            StatHero statHero = statHeroMap.get(game.getHero());
            statHero.setMmr(statHero.getMmr() + game.getDifference());
            statHero.setGamesPlayed(statHero.getGamesPlayed() + 1);
            statHeroMap.replace(game.getHero(), statHero);
        }
        if (statHeroMap.size() > 0){
            max = Collections.max(statHeroMap.values(),
                        (a, b) -> Float.compare(a.getMmr(), b.getMmr()));
            min = Collections.min(statHeroMap.values(),
                    (a, b) -> Float.compare(a.getMmr(), b.getMmr()));
            maxPlayed = Collections.max(statHeroMap.values(),
                    (a, b) -> Float.compare(a.getGamesPlayed(), b.getGamesPlayed()));
            StatHero maxCopy = copy(max);
            StatHero minCopy = copy(min);
            StatHero maxPlayedCopy = copy(maxPlayed);
            stat.setAvgMmrGain(stat.getAvgMmrGain() / stat.getGamesPlayed());
            stat.setAvgMmr(stat.getAvgMmr() / stat.getGamesPlayed());
            stat.setBestHero(maxCopy.getName());
            stat.setBestHeroNumber(maxCopy.getMmr());
            stat.setWorstHero(minCopy.getName());
            stat.setWorstHeroNumber(minCopy.getMmr());
            stat.setMostHero(maxPlayedCopy.getName());
            stat.setMostHeroNumber(maxPlayedCopy.getGamesPlayed());
            stat.setPositions(positions);

        }
        return stat;
    }

    private static StatHero copy( StatHero statHero ) {
        StatHero newStatHero = new StatHero();
        newStatHero.setGamesPlayed(statHero.getGamesPlayed());
        newStatHero.setMmr(statHero.getMmr());
        newStatHero.setName(statHero.getName());
        return newStatHero;
    }
}
