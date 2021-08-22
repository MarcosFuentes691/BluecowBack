package com.bluecow.repository;

import com.bluecow.entity.Game;
import com.bluecow.entity.Hero;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HeroRepository extends JpaRepository<Hero, Long> {

    Hero findByPlayerAndName(String player,String name);
    List<Hero> findAllByPlayer(String player);
}
