package com.bluecow.controller;

import com.bluecow.entity.Game;
import com.bluecow.repository.GameRepository;
import com.bluecow.security.jwt.JwtProvider;
import com.bluecow.service.HeroService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/player")
@CrossOrigin
@Slf4j
public class PlayerController {

    private final HeroService heroService;
    private final GameRepository gameRepository;

    @Autowired
    JwtProvider jwtProvider;

    public PlayerController(HeroService heroService, GameRepository gameRepository) {
        this.heroService = heroService;
        this.gameRepository = gameRepository;
    }

    @GetMapping("/stats")
    public ResponseEntity<List<Game>> viewHeroes(@RequestHeader("Authorization") String authReq){
        return null;
    }
}
