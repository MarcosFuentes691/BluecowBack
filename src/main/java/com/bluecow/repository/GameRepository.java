package com.bluecow.repository;

import com.bluecow.entity.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Calendar;
import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {

    @Query(value = "SELECT * FROM Game WHERE player = ?1 GROUP BY id ORDER BY id DESC",
            countQuery = "SELECT count(*) FROM Game WHERE player = ?1 GROUP BY id ORDER BY id DESC",
            nativeQuery = true)
    Page<Game> findAllByPlayerOrderByIdDesc(String player, Pageable pageable);
    List<Game> findAllByPlayerAndHero(String player,String hero);
    List<Game> findAllByIdIsLessThanAndPlayerOrderByIdDesc(Long id, String player);
    Game getFirstByIdIsLessThanAndPlayerOrderByIdDesc(Long id, String player);
    Game findFirstByIdIsLessThanAndPlayerOrderByIdDesc(Long id, String player);
    @Query(value = "SELECT * FROM Game WHERE player = ?1 and timestamp>?2 and timestamp< ?3 GROUP BY id ORDER BY id DESC",
            countQuery = "SELECT count(*) FROM Game WHERE player = ?1 and timestamp>?2 and timestamp< ?3 GROUP BY id ORDER BY id DESC",
            nativeQuery = true)
    Page<Game> findAllByPlayerAndTimestampAfterAndTimestampBefore(String player, Calendar after, Calendar before, Pageable pageable);
    @Query(value = "SELECT * FROM Game WHERE player = ?1 and timestamp>?2 and timestamp< ?3 and hero=?4 GROUP BY id ORDER BY id DESC",
            countQuery = "SELECT count(*) FROM Game WHERE player = ?1 and timestamp>?2 and timestamp< ?3 GROUP BY id ORDER BY id DESC",
            nativeQuery = true)
    Page<Game> findAllByPlayerAndTimestampAfterAndTimestampBeforeAndHero(String player, Calendar after, Calendar before, String hero, Pageable pageable);
}

