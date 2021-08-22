package com.bluecow.controller;

import com.bluecow.entity.Game;
import com.bluecow.repository.GameRepository;
import com.bluecow.security.jwt.JwtProvider;
import com.bluecow.service.GameService;
import com.bluecow.utility.BearerCleaner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/game")
@CrossOrigin
@Slf4j
public class GameController {

    private final GameService gameService;
    private final GameRepository gameRepository;

    @Autowired
    BearerCleaner bearerCleaner;
    @Autowired
    JwtProvider jwtProvider;

    public GameController(GameService gameService,GameRepository gameRepository) {
        this.gameService = gameService;
        this.gameRepository = gameRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addGame(@RequestHeader("Authorization") String authReq,
                                        @RequestBody Game game){
        authReq=bearerCleaner.cleanBearer(authReq);
        game.setPlayer(authReq);
        try {
            game=gameService.saveGame(game);
        }catch (Exception e){
            log.warn(e.getMessage());
            return new ResponseEntity<>("Game not valid", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @PutMapping("/edit")
    public ResponseEntity<List<Game>> editGame(@RequestHeader("Authorization") String authReq) {
        return null;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteGame(@RequestHeader("Authorization") String authReq,
                                             @PathVariable("id") Long id) {
        authReq=bearerCleaner.cleanBearer(authReq);
        try {
            gameService.deleteGameById(id,authReq);
        }catch (Exception e){
            log.warn(e.getMessage());
            return  new ResponseEntity<>("Id " +id+ " not found", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Game" +id.toString()+ " deleted succesfully", HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detailedGame(@RequestHeader("Authorization") String authReq,
                                          @PathVariable("id") Long id) {
        authReq=bearerCleaner.cleanBearer(authReq);
        Game game = null;
        try {
            game=gameService.detailedGameById(id,authReq);
        }catch (Exception e){
            log.warn(e.getMessage());
            return  new ResponseEntity<>("Id " +id+ " not found", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @GetMapping("/view")
    public ResponseEntity<List<Game>> viewGames(@RequestHeader("Authorization") String authReq){
        authReq=bearerCleaner.cleanBearer(authReq);
        return new ResponseEntity<>(gameService.viewGames(authReq), HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchGames(@RequestHeader("Authorization") String authReq,
                                                  @RequestParam(required=false) String hero,
                                                  @RequestParam(required=false) String from,
                                                  @RequestParam(required=false) String to){
        authReq=bearerCleaner.cleanBearer(authReq);
        Timestamp fromTime=Timestamp.valueOf(from);
        Timestamp toTime=Timestamp.valueOf(to);
        if(toTime.before(fromTime))
            return new ResponseEntity<>("Invalid dates",HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(gameService.searchGames(authReq,hero,fromTime,toTime), HttpStatus.OK);
    }




}
