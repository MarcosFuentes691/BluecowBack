package com.bluecow.service;

import com.bluecow.entity.Game;
import com.bluecow.entity.Hero;
import com.bluecow.repository.GameRepository;
import com.bluecow.repository.HeroRepository;
import com.bluecow.utility.HeroUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class HeroServiceImpl implements HeroService{

    private final GameRepository gameRepository;
    private final HeroRepository heroRepository;

    HeroUtility heroUtility = new HeroUtility();

    @Override
    public Collection<Hero> viewHeroes(String playerEmail, String from, String to) throws Exception {
        return makeHeroes(playerEmail,from,to);
    }


    @Override
    public Hero searchHero(String playerEmail, String stringHero, String from, String to) throws Exception {
        if(!(heroUtility.heroExists(stringHero)))
            throw new Exception("hero doesnt exists");
        if(from==null && to==null)
            return heroRepository.findByPlayerAndName(playerEmail,stringHero);
        Calendar calFrom = Calendar.getInstance();
        Calendar calTo = Calendar.getInstance();
        if(from==null)
            calFrom.setTime(Date.from(Instant.EPOCH));
        else
            calFrom.setTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(from));
        if(to==null)
            calTo.setTime(Timestamp.valueOf(LocalDateTime.of(2030,1,1,1,1)));
        else
            calTo.setTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(to));
        if (calTo.getTime().before(calFrom.getTime()))
            throw new Exception("Invalid dates");
        return makeHero(playerEmail,stringHero,calFrom,calTo);
    }

    private Hero makeHero(String playerEmail, String stringHero, Calendar from, Calendar to){
        List<Game> games = gameRepository.findAllByPlayerAndTimestampAfterAndTimestampBeforeAndHero(playerEmail,from,to,stringHero);
        Hero hero = new Hero();
        float avgPos = 0;
        int mmr = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(1);
        hero.setLastUse(cal);
        for (Game actualGame : games) {
            Game previousGame = gameRepository.getFirstByIdIsLessThanAndPlayerOrderByIdDesc(actualGame.getId(), playerEmail);
            avgPos += actualGame.getPlace();
            if (previousGame == null) {
                mmr += actualGame.getMmr();
            } else {
                mmr += actualGame.getMmr() - previousGame.getMmr();
            }
            if (actualGame.getTimestamp().after(hero.getLastUse()))
                hero.setLastUse(actualGame.getTimestamp());
        }
        hero.setMmr(mmr);
        hero.setAvgPlace(avgPos/games.size());
        hero.setHeroUrl(stringHero);
        hero.setName(stringHero);
        hero.setPlayer(playerEmail);
        hero.setGamesPlayed(games.size());
        return hero;
    }

    private Collection<Hero> makeHeroes(String playerEmail, String from, String to) throws Exception {
        Calendar calFrom = Calendar.getInstance();
        Calendar calTo = Calendar.getInstance();
        if(from==null)
            calFrom.setTime(Date.from(Instant.EPOCH));
        else
            calFrom.setTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(from));
        if(to==null)
            calTo.setTime(Timestamp.valueOf(LocalDateTime.of(2030,1,1,1,1)));
        else
            calTo.setTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(to));
        if (calTo.getTime().before(calFrom.getTime()))
            throw new Exception("Invalid dates");
        Map<String,Hero> heroMap= new HashMap<>();
        List<Game> games = gameRepository.findAllByPlayerAndTimestampAfterAndTimestampBefore(playerEmail,calFrom,calTo);
        for(Game game : games){
            heroMap.put(game.getHero(),new Hero(game.getHero()));
        }
        for (int i=0;i< games.size();i++) {
            Game actualGame = games.get(i);
            Hero gameHero = heroMap.get(actualGame.getHero());
            Game previousGame;
            if(i>0)
                previousGame = games.get(i-1);
            else
                previousGame = null;
            int mmr = 0;
            if (previousGame == null) {
                mmr += actualGame.getMmr();
                gameHero.setAvgPlace((float)actualGame.getPlace());
            } else {
                mmr += actualGame.getMmr() - previousGame.getMmr();
                gameHero.setAvgPlace((gameHero.getAvgPlace() * gameHero.getGamesPlayed() + actualGame.getPlace()) / (gameHero.getGamesPlayed() + 1));
            }
            if (actualGame.getTimestamp().after(heroMap.get(actualGame.getHero()).getLastUse()))
                gameHero.setLastUse(actualGame.getTimestamp());
            gameHero.setMmr(gameHero.getMmr()+mmr);
            gameHero.setGamesPlayed(gameHero.getGamesPlayed()+1);
            heroMap.replace(actualGame.getHero(),gameHero);
        }
        return heroMap.values();
    }
}
