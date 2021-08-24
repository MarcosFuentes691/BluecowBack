package com.bluecow.repository;

import com.bluecow.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findAllByPlayerOrderByIdDesc(String player);
    List<Game> findAllByPlayerAndHero(String player,String hero);
    List<Game> findAllByIdIsLessThanAndPlayerOrderByIdDesc(Long id, String player);
    Game getFirstByIdIsLessThanAndPlayerOrderByIdDesc(Long id, String player);
    List<Game> findAllByPlayerAndTimestampAfterAndTimestampBefore(String player,Calendar after, Calendar before);
    List<Game> findAllByPlayerAndTimestampAfterAndTimestampBeforeAndHero(String player,Calendar after, Calendar before, String hero);

}
