package com.bluecow.controller;

import com.bluecow.entity.Game;
import com.bluecow.entity.Hero;
import com.bluecow.repository.GameRepository;
import com.bluecow.security.jwt.JwtProvider;
import com.bluecow.service.HeroService;
import com.bluecow.utility.BearerCleaner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hero")
@CrossOrigin
@Slf4j
public class HeroController {

    private final HeroService heroService;
    private final GameRepository gameRepository;

    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    BearerCleaner bearerCleaner;

    public HeroController(HeroService heroService, GameRepository gameRepository) {
        this.heroService = heroService;
        this.gameRepository = gameRepository;
    }

    @GetMapping("/detail/{hero}")
    public ResponseEntity<Object> detailedHero(@RequestHeader("Authorization") String authReq,
                                          @PathVariable("hero") String heroString) {
        authReq=bearerCleaner.cleanBearer(authReq);
        Hero hero;
        try {
            hero=heroService.viewHero(authReq, heroString);
        }
        catch(Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.status(200).body(hero);
    }

    @GetMapping("/all")
    public ResponseEntity<?> viewHeroes(@RequestHeader("Authorization") String authReq){
        String email="Holi";
        authReq=bearerCleaner.cleanBearer(authReq);
        List<Hero> heroes=null;
        try {
            heroes=heroService.viewHeroes(email);
        }
        catch(Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.status(200).body(heroes);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchHero(@RequestHeader("Authorization") String authReq,
                                         @RequestParam(required=false) String hero,
                                         @RequestParam(required=false) String from,
                                         @RequestParam(required=false) String to){
        authReq=bearerCleaner.cleanBearer(authReq);
        try {
            return new ResponseEntity<>(heroService.searchHero(authReq, hero, from, to), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}