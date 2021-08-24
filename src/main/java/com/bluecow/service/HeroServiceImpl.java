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
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class HeroServiceImpl implements HeroService{

    private final GameRepository gameRepository;
    private final HeroRepository heroRepository;

    HeroUtility heroUtility = new HeroUtility();

    @Override
    public Hero viewHero(String playerEmail, String stringHero) throws Exception {
        if(!(heroUtility.heroExists(stringHero)))
            throw new Exception("hero doesnt exists");
        if(heroRepository.findByPlayerAndName(playerEmail,stringHero)==null)
            throw new Exception("hero not found");
        return heroRepository.findByPlayerAndName(playerEmail, stringHero);
    }

    @Override
    public List<Hero> viewHeroes(String playerEmail) throws Exception {
        return heroRepository.findAllByPlayer(playerEmail);
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
            calFrom.setTime(Timestamp.valueOf(LocalDateTime.of(2014,1,1,1,1)));
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

    @Override
    public void updateHero(Game game,boolean operation) throws Exception {
        if(!(heroUtility.heroExists(game.getHero())))   ////This two exceptions shouldnt pop theoretically??
            throw new Exception("hero doesnt exists");
        if(heroRepository.findByPlayerAndName(game.getPlayer(), game.getHero())==null)
            throw new Exception("hero not found");
        Hero hero = heroRepository.findByPlayerAndName(game.getPlayer(), game.getHero());
        if(operation){
            hero.setMmr(hero.getMmr()+game.getMmr());
            hero.setAvgPlace((hero.getAvgPlace()*hero.getGamesPlayed()+ game.getPlace())/(hero.getGamesPlayed()+1));
            hero.setGamesPlayed(hero.getGamesPlayed()+1);
            hero.setLastUse(game.getTimestamp()); //SHOULD COMPARE

        }
        else{
            hero.setMmr(hero.getMmr()-game.getMmr());
            hero.setAvgPlace((hero.getAvgPlace()*hero.getGamesPlayed()- game.getPlace())/(hero.getGamesPlayed()-1));
            hero.setGamesPlayed(hero.getGamesPlayed()-1);
            hero.setLastUse(game.getTimestamp()); //SEARCH LASTONE
        }
        heroRepository.save(hero);
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

    /*private void newHero(String playerEmail, String stringHero,){
        List<Game> games = gameRepository.findAllByPlayerAndTimestampAfterAndTimestampBeforeAndHero(playerEmail,from,to,stringHero);
        Hero hero = new Hero();
        float avgPos = 0;
        int mmr = 0;
        hero.setLastUse(null);//(Timestamp.valueOf(LocalDateTime.of(2014,1,1,1,1)));
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
        heroRepository.save(hero);
    }*/
}


