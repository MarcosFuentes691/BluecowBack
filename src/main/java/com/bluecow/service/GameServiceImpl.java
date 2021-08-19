package com.bluecow.service;

import com.bluecow.HeroUtility;
import com.bluecow.entity.Game;
import com.bluecow.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class GameServiceImpl implements GameService{

    HeroUtility heroUtility;
    private final GameRepository gameRepository;

    @Override
    public Game saveGame(Game game){
        //if(game.getPlace()>8 && game.getPlace()<1)
            //throw new Exception("Place not correct");
        //if(!(heroUtility.heroExists(game.getHero())))
            //throw new Exception("Hero not correct");
        return gameRepository.save(game);
    }

    @Override
    public List<Game> viewGames(String playerEmail) {
        return gameRepository.findAllByPlayer(playerEmail);
    }

    @Override
    public List<Game> searchGames(String playerEmail, String hero, Object from, Object to) {
        //implementar from/to
        return gameRepository.findAllByPlayerAndHero(playerEmail,hero);
    }
}
