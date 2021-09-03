package com.bluecow.service;

import com.bluecow.utility.HeroUtility;
import com.bluecow.entity.Game;
import com.bluecow.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
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
            throw new Exception("Timestamp from the future");
        if(!(heroUtility.heroExists(game.getHero())))
            throw new Exception("Hero not correct");
        try{
            Game prevGame=gameRepository.getFirstByTimestampIsLessThanAndPlayerOrderByTimestampDesc(game.getTimestamp(), game.getPlayer());
            int diff=game.getMmr()-prevGame.getMmr();
            game.setDifference(diff);
        }catch (Exception e) {
            e.printStackTrace();
            game.setDifference(0);
        }
        return gameRepository.save(game);
    }

    @Override
    public boolean deleteGameById(Long id,String player) throws Exception {
        if(gameRepository.findById(id).isEmpty())
            throw new Exception("Game not found");
        if(!(player.equals(gameRepository.findById(id).get().getPlayer())))
            throw new Exception("Not allowed");
        Game nextGame=gameRepository.getFirstByTimestampIsGreaterThanAndPlayerOrderByTimestampAsc(gameRepository.findById(id).get().getTimestamp(), gameRepository.findById(id).get().getPlayer());
        Game prevGame=gameRepository.getFirstByTimestampIsLessThanAndPlayerOrderByTimestampDesc(gameRepository.findById(id).get().getTimestamp(), gameRepository.findById(id).get().getPlayer());
        nextGame.setDifference(nextGame.getMmr()-prevGame.getMmr());
        gameRepository.save(nextGame);
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
    public List<Game> viewGames(String playerEmail,int page,int amount) {
        return gameRepository.findAllGames(playerEmail,PageRequest.of(page, amount)).getContent();
    }


    @Override
    public List<Game> searchGames(String playerEmail, String hero, String from, String to,int page,int amount,String timeZone) throws Exception {
        if(hero.equals("All")||hero.equals("all"))
            hero=null;
        if(hero!=null && !(heroUtility.heroExists(hero)))
            throw new Exception("hero doesnt exists");
        int timeZoneInt=0;
        if(!(to.equals("now"))){
            timeZone=timeZone.substring(0,3);
            timeZoneInt=Integer.parseInt(timeZone);
            timeZoneInt=60*(-timeZoneInt);
        }else
            timeZoneInt=Integer.parseInt(timeZone);
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
        log.info(calFrom.getTime().toString());
        log.info(calTo.getTime().toString());
        //calFrom.add(Calendar.MINUTE,timeZoneInt);
        //calTo.add(Calendar.MINUTE,timeZoneInt);
        if (calTo.getTime().before(calFrom.getTime()))
                throw new Exception("Invalid dates");
        List<Game> games;
        if(hero==null)
            games= gameRepository.findGamesInPeriod(playerEmail,calFrom,calTo,PageRequest.of(page, amount)).getContent();
        else {
            games= gameRepository.findGamesInPeriodWithHero(playerEmail, calFrom, calTo, hero, PageRequest.of(page, amount)).getContent();
        }
        return games;
    }
}
