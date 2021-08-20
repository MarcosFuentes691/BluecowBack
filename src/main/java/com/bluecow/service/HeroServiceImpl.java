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

import java.util.ArrayList;
import java.util.List;

import static com.bluecow.consts.ConstHeroes.heroList;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class HeroServiceImpl implements HeroService{

    private final GameRepository gameRepository;

    HeroUtility heroUtility;
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
        for (int i=0;i<heroList.size();i++) {
            heroes.add(makeHero(playerEmail,heroList.get(i)));
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
        for(int i=0;i<games.size();i++) {
            avgPos += games.get(i).getPlace();
            mmr += games.get(i).getMmr() - gameRepository.findByPlayerAndIdBefore(playerEmail, games.get(i).getId()).getMmr();
            if (games.get(i).getTimestamp().after(hero.getLastUse()))
                hero.setLastUse(games.get(i).getTimestamp());
        }
        hero.setHeroUrl(stringHero);//heroUtility.);
        hero.setName(stringHero);
        return hero;
    }

}
