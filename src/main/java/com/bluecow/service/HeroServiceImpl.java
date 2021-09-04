package com.bluecow.service;

import com.bluecow.entity.Game;
import com.bluecow.entity.Hero;
import com.bluecow.repository.GameRepository;
import com.bluecow.utility.HeroUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class HeroServiceImpl implements HeroService{

    private final GameRepository gameRepository;

    HeroUtility heroUtility = new HeroUtility();

    @Override
    public Collection<Hero> viewHeroes(String playerEmail, String from, String to,String timeZone) throws Exception {
        return makeHeroes(playerEmail,from,to,timeZone);
    }


    @Override
    public Hero searchHero(String playerEmail, String stringHero, String from, String to,String timeZone) throws Exception {
        if(!(heroUtility.heroExists(stringHero)))
            throw new Exception("hero doesnt exists");
        int timeZoneInt=0;
        if(!(to.equals("now"))){
            timeZone=timeZone.substring(0,3);
            log.info(String.valueOf(timeZone.charAt(0)));
            if(timeZone.charAt(0) != '-') {
                timeZone = timeZone.substring(1);
            }
            timeZoneInt=Integer.parseInt(timeZone);
            timeZoneInt=60*(-timeZoneInt);
        }else
            timeZoneInt=Integer.parseInt(timeZone);
        timeZoneInt-=180; ///////TODO remove this when is in heroku
        Calendar calFrom = Calendar.getInstance();
        Calendar calTo = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        if(from.equals("Last week"))
            calFrom.add(Calendar.DATE,-6);
        else if(from.equals("Last month"))
            calFrom.add(Calendar.MONTH,-1);
        else if(from.equals("Always"))
            calFrom.add(Calendar.YEAR,-20);
        else if(!(from.equals("Today")))
            calFrom.setTime(sdf.parse(from));
        if(!(to.equals("now")))
            calTo.setTime(sdf.parse(to));
        calFrom.set(Calendar.HOUR_OF_DAY,0);
        calFrom.set(Calendar.MINUTE,0);
        calFrom.set(Calendar.SECOND,0);
        calFrom.add(Calendar.MINUTE,timeZoneInt);
        calTo.add(Calendar.MINUTE,timeZoneInt);
        if (calTo.getTime().before(calFrom.getTime()))
            throw new Exception("Invalid dates");
        return makeHero(playerEmail,stringHero,calFrom,calTo);
    }

    private Hero makeHero(String playerEmail, String stringHero, Calendar from, Calendar to){
        List<Game> games = gameRepository.findGamesInPeriodWithHero(playerEmail,from,to,stringHero, PageRequest.of(0,50000)).getContent();
        Hero hero = new Hero();
        float avgPos = 0;
        int mmr = 0;
        int[] positions = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(1);
        hero.setLastUse(cal);
        for (Game actualGame : games) {
            avgPos += actualGame.getPlace();
            mmr += actualGame.getDifference();
            if (actualGame.getTimestamp().after(hero.getLastUse()))
                hero.setLastUse(actualGame.getTimestamp());
            positions[actualGame.getPlace()-1]++;
        }
        hero.setMmr(mmr);
        hero.setPositions(positions);
        hero.setAvgPlace(avgPos/games.size());
        hero.setName(stringHero);
        hero.setPlayer(playerEmail);
        hero.setGamesPlayed(games.size());
        return hero;
    }

    private Collection<Hero> makeHeroes(String playerEmail, String from, String to,String timeZone) throws Exception {
        int timeZoneInt=0;
        if(!(to.equals("now"))){
            timeZone=timeZone.substring(0,3);
            log.info(String.valueOf(timeZone.charAt(0)));
            if(timeZone.charAt(0) != '-') {
                timeZone = timeZone.substring(1);
            }
            timeZoneInt=Integer.parseInt(timeZone);
            timeZoneInt=60*(-timeZoneInt);
        }else
            timeZoneInt=Integer.parseInt(timeZone);
        timeZoneInt-=180; ///////TODO remove this when is in heroku
        Calendar calFrom = Calendar.getInstance();
        Calendar calTo = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        if(from.equals("Last week"))
            calFrom.add(Calendar.DATE,-6);
        else if(from.equals("Last month"))
            calFrom.add(Calendar.MONTH,-1);
        else if(from.equals("Always"))
            calFrom.add(Calendar.YEAR,-20);
        else if(!(from.equals("Today")))
            calFrom.setTime(sdf.parse(from));
        if(!(to.equals("now")))
            calTo.setTime(sdf.parse(to));
        calFrom.set(Calendar.HOUR_OF_DAY,0);
        calFrom.set(Calendar.MINUTE,0);
        calFrom.set(Calendar.SECOND,0);
        calFrom.add(Calendar.MINUTE,timeZoneInt);
        calTo.add(Calendar.MINUTE,timeZoneInt);
        log.info(calFrom.getTime().toString());
        log.info(calTo.getTime().toString());
        if (calTo.getTime().before(calFrom.getTime()))
            throw new Exception("Invalid dates");
        Map<String,Hero> heroMap= new HashMap<>();
        List<Game> games = gameRepository.findGamesInPeriod(playerEmail,calFrom,calTo,PageRequest.of(0,50000)).getContent();
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
            if (previousGame == null) {
                gameHero.setAvgPlace((float)actualGame.getPlace());
            } else {
                gameHero.setAvgPlace((gameHero.getAvgPlace() * gameHero.getGamesPlayed() + actualGame.getPlace()) / (gameHero.getGamesPlayed() + 1));
            }
            if (actualGame.getTimestamp().after(heroMap.get(actualGame.getHero()).getLastUse()))
                gameHero.setLastUse(actualGame.getTimestamp());
            gameHero.setMmr(gameHero.getMmr()+ actualGame.getDifference());
            gameHero.setGamesPlayed(gameHero.getGamesPlayed()+1);
            heroMap.replace(actualGame.getHero(),gameHero);
        }
        return heroMap.values();
    }
}
