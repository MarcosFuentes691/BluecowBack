package com.bluecow.service;

import com.bluecow.entity.Game;
import com.bluecow.entity.Hero;

import java.util.ArrayList;
import java.util.List;

public interface HeroService {
    Hero viewHero(String playerEmail, String hero) throws Exception;
    List<Hero> viewHeroes(String playerEmail) throws Exception;
    Hero searchHero(String playerEmail, String hero, Object from, Object to) throws Exception;
    void updateHero(Game game,boolean operation) throws Exception;
}
