package com.bluecow.controller;

import com.bluecow.entity.Game;
import com.bluecow.repository.GameRepository;
import com.bluecow.security.jwt.JwtProvider;
import com.bluecow.service.HeroService;
import com.bluecow.service.PlayerService;
import com.bluecow.utility.BearerCleaner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/player")
@CrossOrigin
@Slf4j
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    BearerCleaner bearerCleaner;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/stats")
    public ResponseEntity<?> viewHeroes(@RequestHeader("Authorization") String authReq){
        authReq=bearerCleaner.cleanBearer(authReq);
        try {
            return new ResponseEntity<>(playerService.getPlayerStats(authReq),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
