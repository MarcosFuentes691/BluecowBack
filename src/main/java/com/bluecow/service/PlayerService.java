package com.bluecow.service;

import com.bluecow.consts.ConstHeroes;
import com.bluecow.entity.Game;
import com.bluecow.entity.Hero;
import com.bluecow.entity.Player;
import com.bluecow.entity.Stats;
import com.bluecow.repository.GameRepository;
import com.bluecow.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class PlayerService {

    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    GameRepository gameRepository;
    Map<Integer,String> heroesMmr = new TreeMap<Integer,String>();
    Map<Integer,String> heroesGames = new TreeMap<Integer,String>();

    public Optional<Player> getByEmail(String email){
        return playerRepository.findByEmail(email);
    }

    public boolean existsEmail(String email){
        return playerRepository.existsByEmail(email);
    }

    public Player save(Player player){
        return playerRepository.save(player);
    }

    public ArrayList<Stats> getPlayerStats(String email){
        Stats stat= makeStats(email,null);
        return null;
    }

    private Stats makeStats(String email, Stats prevStats){
        if(prevStats==null){
            Calendar now = Calendar.getInstance();
            Calendar yest = Calendar.getInstance();
            yest.add(Calendar.DATE,-1);
            List<Game> games = gameRepository.findAllByPlayerAndTimestampAfterAndTimestampBefore(email, yest, now);
            for(String hero : ConstHeroes.heroList){
                heroesMmr.put(0,hero);
                heroesGames.put(0,hero);
            }
            Stats stat = new Stats("new stat");
        }
        else {
            //2,3,4 iteration
        }
        return null;
    }
}
