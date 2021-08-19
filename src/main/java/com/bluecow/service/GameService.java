package com.bluecow.service;

import com.bluecow.entity.Game;

import java.util.Collection;
import java.util.List;

public interface GameService {
    Game saveGame(Game game);
    List<Game> viewGames(String playerEmail);
    List<Game> searchGames(String playerEmail,String hero,Object from,Object to);
}
