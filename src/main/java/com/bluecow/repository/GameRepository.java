package com.bluecow.repository;

import com.bluecow.entity.Game;
import com.bluecow.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findAllByPlayer(String player);
    List<Game> findAllByPlayerAndHero(String player,String hero);
}
