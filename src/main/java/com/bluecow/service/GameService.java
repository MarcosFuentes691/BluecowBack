package com.bluecow.service;

import com.bluecow.entity.Game;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

public interface GameService {
    Game saveGame(Game game) throws Exception;
    //boolean deleteGame(Game game) throws Exception;
    boolean deleteGameById(Long id,String player) throws Exception;
    //boolean detailedGame(Game game) throws Exception;
    Game detailedGameById(Long id,String player) throws Exception;
    List<Game> viewGames(String playerEmail);
    List<Game> searchGames(String playerEmail, String hero, Timestamp from, Timestamp to);
}
