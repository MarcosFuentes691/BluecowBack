package com.bluecow.repository;

import com.bluecow.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findAllByPlayer(String player);
    List<Game> findAllByPlayerAndHero(String player,String hero);
    List<Game> findAllByIdIsLessThanAndPlayerOrderByIdDesc(Long id, String player);
    Game getFirstByIdIsLessThanAndPlayerOrderByIdDesc(Long id, String player);
    List<Game> findAllByTimestampAfterAndTimestampBefore(Timestamp after, Timestamp before);
    List<Game> findAllByTimestampAfterAndTimestampBeforeAndHero(Timestamp after, Timestamp before, String hero);

}
