package com.bluecow.service;

import com.bluecow.entity.Hero;

import java.util.ArrayList;

public interface HeroService {
    Hero viewHero(String playerEmail, String hero) throws Exception;
    ArrayList<Hero> viewHeroes(String playerEmail) throws Exception;
    Hero searchHero(String playerEmail, String hero, Object from, Object to) throws Exception;
}
