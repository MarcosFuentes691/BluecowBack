package com.bluecow.controller;

import com.bluecow.entity.Game;
import com.bluecow.repository.GameRepository;
import com.bluecow.security.jwt.JwtProvider;
import com.bluecow.service.GameService;
import com.bluecow.utility.BearerCleaner;
import io.swagger.annotations.ApiOperation;
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

    @Autowired
    BearerCleaner bearerCleaner;
    @Autowired
    JwtProvider jwtProvider;

    public GameController(GameService gameService,GameRepository gameRepository) {
        this.gameService = gameService;
    }

    @ApiOperation(value = "Add a new game")
    @PostMapping("/add")
    public ResponseEntity<?> addGame(@RequestHeader("Authorization") String authReq,
                                        @RequestBody Game game){
        authReq=bearerCleaner.cleanBearer(authReq);
        game.setPlayer(authReq);
        game.setHeroUrl("none");
        game.setTimestampString("none");
        try {
            game=gameService.saveGame(game);
        }catch (Exception e){
            log.warn(e.getMessage());
            return new ResponseEntity<>("Game not valid", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @ApiOperation(value = "Edit a game")
    @PutMapping("/edit")
    public ResponseEntity<List<Game>> editGame(@RequestHeader("Authorization") String authReq) {
        return null;
    }

    @ApiOperation(value = "Delete a game")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteGame(@RequestHeader("Authorization") String authReq,
                                             @RequestParam("id") Long id) {
        authReq=bearerCleaner.cleanBearer(authReq);
        try {
            gameService.deleteGameById(id,authReq);
        }catch (Exception e){
            log.warn(e.getMessage());
            return  new ResponseEntity<>("Id " +id+ " not found", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Game" +id.toString()+ " deleted succesfully", HttpStatus.OK);
    }

    @ApiOperation(value = "View a page of all the games of the user")
    @GetMapping("/view")
    public ResponseEntity<List<Game>> viewGames(@RequestHeader("Authorization") String authReq,
                                                @RequestParam int page,
                                                @RequestParam int amount){
        authReq=bearerCleaner.cleanBearer(authReq);
        return new ResponseEntity<>(gameService.viewGames(authReq,page,amount), HttpStatus.OK);
    }

    @ApiOperation(value = "View a page of a specific search of the games of the user")
    @GetMapping("/search")
    public ResponseEntity<?> searchGames(@RequestHeader("Authorization") String authReq,
                                         @RequestParam(required=false) String hero,
                                         @RequestParam(required=false) String from,
                                         @RequestParam(required=false) String to,
                                         @RequestParam(required=false) String timeZone,
                                         @RequestParam(required=false) int page,
                                         @RequestParam(required=false) int amount){
        authReq=bearerCleaner.cleanBearer(authReq);
        try {
            return new ResponseEntity<>(gameService.searchGames(authReq, hero, from, to,page,amount,timeZone), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

}
