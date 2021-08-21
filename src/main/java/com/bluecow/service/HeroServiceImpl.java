package com.bluecow.service;

import com.bluecow.consts.ConstHeroes;
import com.bluecow.entity.Game;
import com.bluecow.entity.Hero;
import com.bluecow.repository.GameRepository;
import com.bluecow.utility.HeroUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.bluecow.consts.ConstHeroes.heroList;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class HeroServiceImpl implements HeroService{

    private final GameRepository gameRepository;

    HeroUtility heroUtility = new HeroUtility();
    ConstHeroes constHeroes;

    @Override
    public Hero viewHero(String playerEmail, String stringHero) throws Exception {
        if(!(heroUtility.heroExists(stringHero)))
            throw new Exception("hero doesnt exists");
        return makeHero(playerEmail,stringHero);
    }

    @Override
    public ArrayList<Hero> viewHeroes(String playerEmail) throws Exception {
        ArrayList<Hero> heroes = new ArrayList<>();
        for (String s : heroList) {
            heroes.add(makeHero(playerEmail, s));
        }
        return heroes;
    }


    @Override
    public Hero searchHero(String playerEmail, String stringHero, Object from, Object to) throws Exception {
        if(!(heroUtility.heroExists(stringHero)))
            throw new Exception("hero doesnt exists");
        return null;
    }

    private Hero makeHero(String playerEmail, String stringHero){
        List<Game> games = gameRepository.findAllByPlayerAndHero(playerEmail,stringHero);
        Hero hero = new Hero();
        float avgPos = 0;
        int mmr = 0;
        hero.setLastUse(Timestamp.valueOf(LocalDateTime.of(2014,1,1,1,1)));
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
        hero.setGamesPlayed(games.size());
        return hero;
    }
}


