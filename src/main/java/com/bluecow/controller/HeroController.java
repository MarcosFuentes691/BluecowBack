package com.bluecow.controller;

import com.bluecow.entity.Game;
import com.bluecow.entity.Hero;
import com.bluecow.repository.GameRepository;
import com.bluecow.security.jwt.JwtProvider;
import com.bluecow.service.HeroService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    public HeroController(HeroService heroService, GameRepository gameRepository) {
        this.heroService = heroService;
        this.gameRepository = gameRepository;
    }

    @GetMapping("/detail/{hero}")
    public ResponseEntity<Object> detailedHero(@RequestHeader("Authorization") String authReq,
                                          @PathVariable("hero") String heroString) {
        String email="Holi";
        if (authReq != null && authReq.startsWith("Bearer ")) {
            authReq = authReq.replace("Bearer ", "");
            email = jwtProvider.getEmailFromToken(authReq);
            log.info("email=" + email);
        }
        Hero hero;
        try {
            hero=heroService.viewHero(email, heroString);
        }
        catch(Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.status(200).body(hero);
    }

    @GetMapping("/all")
    public ResponseEntity<?> viewHeroes(@RequestHeader("Authorization") String authReq){
        String email="Holi";
        if (authReq != null && authReq.startsWith("Bearer ")) {
            authReq = authReq.replace("Bearer ", "");
            email = jwtProvider.getEmailFromToken(authReq);
            log.info("email=" + email);
        }
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
    public ResponseEntity<List<Game>> searchHeroes(@RequestHeader("Authorization") String authReq){
        return null;
    }
}