package com.bluecow.service;

import com.bluecow.consts.ConstHeroes.*;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.bluecow.consts.ConstHeroes.heroList;


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
        ///makehero
        //if(heroRepository.findByPlayerAndName(playerEmail,stringHero)==null)
        //    throw new Exception("hero not found");
        //return heroRepository.findByPlayerAndName(playerEmail, stringHero);
        return null;//make
    }

    @Override
    public List<Hero> viewHeroes(String playerEmail,String from, String to) throws Exception {
        ///Make heroes on the go
        return makeHeroes(playerEmail,from,to);//heroRepository.findAllByPlayer(playerEmail);
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

    @Override
    public void updateHero(Game game,boolean operation) throws Exception {
        //make heroes on the go
    }
        /*if (!(heroUtility.heroExists(game.getHero())))   ////This two exceptions shouldnt pop theoretically??
            throw new Exception("hero doesnt exists");
        if (heroRepository.findByPlayerAndName(game.getPlayer(), game.getHero()) == null) {
            newHero(game.getPlayer(), game.getHero());
        } else {
            Hero hero = heroRepository.findByPlayerAndName(game.getPlayer(), game.getHero());
            if (operation) {
                hero.setMmr(hero.getMmr() + game.getMmr());
                hero.setAvgPlace((hero.getAvgPlace() * hero.getGamesPlayed() + game.getPlace()) / (hero.getGamesPlayed() + 1));
                hero.setGamesPlayed(hero.getGamesPlayed() + 1);
                hero.setLastUse(game.getTimestamp()); //SHOULD COMPARE
            } else {
                hero.setMmr(hero.getMmr() - game.getMmr());
                hero.setAvgPlace((hero.getAvgPlace() * hero.getGamesPlayed() - game.getPlace()) / (hero.getGamesPlayed() - 1));
                hero.setGamesPlayed(hero.getGamesPlayed() - 1);
                hero.setLastUse(game.getTimestamp()); //SEARCH LASTONE
            }
            heroRepository.save(hero);
        }
    }
*/
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

    private ArrayList<Hero> makeHeroes(String playerEmail, String from, String to) throws Exception {
        Calendar calFrom = Calendar.getInstance();
        Calendar calTo = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
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
        ArrayList<Hero> heroes = new ArrayList<>();
        for(String hero : heroList) {
            heroes.add(new Hero(hero));
        }
        List<Game> games = gameRepository.findAllByPlayerAndTimestampAfterAndTimestampBefore(playerEmail,calFrom,calTo);
        int heroPos = 0;
        for (Game actualGame : games) {
            for(int i=0;i<heroes.size();i++) {
                if(heroes.get(i).getName().equals(actualGame.getHero())) {
                    heroPos=i;
                    break;
                }
            }
            Game previousGame = gameRepository.getFirstByIdIsLessThanAndPlayerOrderByIdDesc(actualGame.getId(), playerEmail);
            int mmr = 0;
            if (previousGame == null) {
                mmr += actualGame.getMmr();
            } else {
                mmr += actualGame.getMmr() - previousGame.getMmr();
            }
            if (actualGame.getTimestamp().after(heroes.get(heroPos).getLastUse()))
                heroes.get(heroPos).setLastUse(actualGame.getTimestamp());
            heroes.get(heroPos).setMmr(heroes.get(heroPos).getMmr()+mmr);
            heroes.get(heroPos).setGamesPlayed(heroes.get(heroPos).getGamesPlayed()+1);
            heroes.get(heroPos).setAvgPlace((heroes.get(heroPos).getAvgPlace() * heroes.get(heroPos).getGamesPlayed() - actualGame.getPlace()) / (heroes.get(heroPos).getGamesPlayed() + 1));
        }
        return heroes;
    }
}
