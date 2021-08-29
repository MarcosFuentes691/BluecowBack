package com.bluecow.service;

import com.bluecow.entity.Game;

import java.util.List;

public interface GameService {
    Game saveGame(Game game) throws Exception;
    boolean deleteGameById(Long id,String player) throws Exception;
    Game detailedGameById(Long id,String player) throws Exception;
    List<Game> viewGames(String playerEmail,int page,int amount);
    List<Game> searchGames(String playerEmail, String hero, String from, String to,int page,int amount, String timeZone) throws Exception;
}
