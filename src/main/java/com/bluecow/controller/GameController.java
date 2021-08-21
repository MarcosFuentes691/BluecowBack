package com.bluecow.controller;

import com.bluecow.entity.Game;
import com.bluecow.repository.GameRepository;
import com.bluecow.security.jwt.JwtProvider;
import com.bluecow.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/game")
@CrossOrigin
@Slf4j
public class GameController {

    private final GameService gameService;
    private final GameRepository gameRepository;

    @Autowired
    JwtProvider jwtProvider;

    public GameController(GameService gameService,GameRepository gameRepository) {
        this.gameService = gameService;
        this.gameRepository = gameRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<Game> addGame(@RequestHeader("Authorization") String authReq,@RequestBody Game game){
        try{
            if(authReq != null && authReq.startsWith("Bearer ")) {
                authReq = authReq.replace("Bearer ", "");
                authReq = jwtProvider.getEmailFromToken(authReq);
            }
        } catch (Exception e){
            log.warn("exception on token, controller");
        }
        try {
            gameService.saveGame(game);
        }catch (Exception e){
            log.warn("exception on saving game, controller");
        }
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @PutMapping("/edit")
    public ResponseEntity<List<Game>> deleteGame(@RequestHeader("Authorization") String authReq) {
        return null;
    }

    @DeleteMapping("/delete")
    public ResponseEntity<List<Game>> editGame(@RequestHeader("Authorization") String authReq) {
        return null;
    }

    @GetMapping("/detail")
    public ResponseEntity<List<Game>> detailedGame(@RequestHeader("Authorization") String authReq) {
        return null;
    }

    @GetMapping("/view")
    public ResponseEntity<List<Game>> viewGames(@RequestHeader("Authorization") String authReq){
        if(authReq != null && authReq.startsWith("Bearer ")) {
                authReq = authReq.replace("Bearer ", "");
                authReq = jwtProvider.getEmailFromToken(authReq);
                log.info("authreq="+authReq);
            }
        log.info(gameRepository.getFirstByIdIsLessThanAndPlayerOrderByIdDesc(200L, "marcosfuentes691@gmail.com").getHero());
        log.info(gameRepository.getFirstByIdIsLessThanAndPlayerOrderByIdDesc(10L, "marcosfuentes691@gmail.com").getTimestamp().toString());
        return new ResponseEntity<>(gameService.viewGames(authReq), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Game>> searchGames(@RequestHeader("Authorization") String authReq,
                                                @RequestParam("hero") String hero,
                                                @RequestParam("from") Object from,
                                                @RequestParam("to") Object to){
        if(authReq != null && authReq.startsWith("Bearer ")) {
            authReq = authReq.replace("Bearer ", "");
            authReq = jwtProvider.getEmailFromToken(authReq);
            log.info("authreq=" + authReq);
        }
        return new ResponseEntity<>(gameService.searchGames(authReq,hero,from,to), HttpStatus.OK);
    }




}
