package com.bluecow.service;

import com.bluecow.entity.Hero;

import java.util.Collection;

public interface HeroService {
    Collection<Hero> viewHeroes(String playerEmail, String from, String to,String timeZone) throws Exception;
    Hero searchHero(String playerEmail, String hero, String from, String to,String timeZone) throws Exception;
}
