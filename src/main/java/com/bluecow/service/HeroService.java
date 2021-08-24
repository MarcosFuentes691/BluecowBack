package com.bluecow.service;

import com.bluecow.entity.Game;
import com.bluecow.entity.Hero;

import java.util.ArrayList;
import java.util.List;

public interface HeroService {
    Hero viewHero(String playerEmail, String hero) throws Exception;
    List<Hero> viewHeroes(String playerEmail,String from,String to) throws Exception;
    Hero searchHero(String playerEmail, String hero, String from, String to) throws Exception;
    void updateHero(Game game,boolean operation) throws Exception;
}
