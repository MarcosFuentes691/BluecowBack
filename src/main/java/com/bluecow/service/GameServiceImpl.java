package com.bluecow.service;

import com.bluecow.utility.HeroUtility;
import com.bluecow.entity.Game;
import com.bluecow.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class GameServiceImpl implements GameService{

    @Autowired
    HeroUtility heroUtility;

    private final GameRepository gameRepository;
    private final HeroService heroService;

    @Override
    public Game saveGame(Game game) throws Exception {
        if(game.getPlace()>8 || game.getPlace()<1)
            throw new Exception("Place not correct");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        Calendar cal = Calendar.getInstance();
        if(game.getTimestampString().equals("none"))
            cal.setTime(game.getTimestamp().getTime());
        else
            cal.setTime(sdf.parse(game.getTimestampString()));
        game.setTimestamp(cal);
        if(game.getTimestamp().after(Calendar.getInstance().getTime()))
            throw new Exception("Timestamp from the future"); //Why this doesnt work????
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
        return gameRepository.findAllByPlayerOrderByIdDesc(playerEmail);
    }


    @Override
    public List<Game> searchGames(String playerEmail, String hero, String from, String to) throws Exception {
        if(!(heroUtility.heroExists(hero)))
            throw new Exception("hero doesnt exists");
        Calendar calFrom = Calendar.getInstance();
        Calendar calTo = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        calFrom.setTime(sdf.parse(from));
        calTo.setTime(sdf.parse(to));
        if (calTo.getTime().before(calFrom.getTime()))
                throw new Exception("Invalid dates");
        if(from==null)
            calFrom.setTime(Timestamp.valueOf(LocalDateTime.of(2014,1,1,1,1)));
        if(to==null)
            calTo.setTime(Timestamp.valueOf(LocalDateTime.of(2030,1,1,1,1)));
        if(hero==null)
            return gameRepository.findAllByPlayerAndTimestampAfterAndTimestampBefore(playerEmail,calFrom,calTo);
        else
            return gameRepository.findAllByPlayerAndTimestampAfterAndTimestampBeforeAndHero(playerEmail,calFrom,calTo,hero);
    }
}
