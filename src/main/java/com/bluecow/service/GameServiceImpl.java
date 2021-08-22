package com.bluecow.service;

import com.bluecow.utility.BearerCleaner;
import com.bluecow.utility.HeroUtility;
import com.bluecow.entity.Game;
import com.bluecow.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class GameServiceImpl implements GameService{

    @Autowired
    HeroUtility heroUtility;
    private final GameRepository gameRepository;

    @Override
    public Game saveGame(Game game) throws Exception {
        if(game.getPlace()>8 || game.getPlace()<1)
            throw new Exception("Place not correct");
        if(game.getTimestamp().after(Timestamp.from(Instant.now().plusSeconds(30))))
            throw new Exception("Timestamp from the future");
        if(!(heroUtility.heroExists(game.getHero())))
            throw new Exception("Hero not correct");
        return gameRepository.save(game);
    }

    @Override
    public boolean deleteGameById(Long id,String player) throws Exception {
        if(gameRepository.findById(id).isEmpty())
            throw new Exception("Game not found");
        if(!(player.equals(gameRepository.findById(id).get().getPlayer())))
            throw new Exception("Not allowed");
        gameRepository.delete(gameRepository.findById(id).get());
        return true;
    }

    @Override
    public Game detailedGameById(Long id, String player) throws Exception {
        if(gameRepository.findById(id).isEmpty())
            throw new Exception("Game not found");
        if(!(player.equals(gameRepository.findById(id).get().getPlayer())))
            throw new Exception("Not allowed");
        return gameRepository.findById(id).get();
    }

    @Override
    public List<Game> viewGames(String playerEmail) {
        return gameRepository.findAllByPlayer(playerEmail);
    }

    @Override
    public List<Game> searchGames(String playerEmail, String hero, Timestamp from, Timestamp to) {
        if(from==null)
            from=Timestamp.valueOf(LocalDateTime.of(2014,1,1,1,1));
        if(to==null)
            to=Timestamp.valueOf(LocalDateTime.of(2030,1,1,1,1));
        if(hero==null)
            return gameRepository.findAllByTimestampAfterAndTimestampBefore(from,to);
        else
            return gameRepository.findAllByTimestampAfterAndTimestampBeforeAndHero(from,to,hero);
    }
}
