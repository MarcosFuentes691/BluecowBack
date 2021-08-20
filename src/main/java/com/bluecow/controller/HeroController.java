package com.bluecow.controller;

import com.bluecow.entity.Game;
import com.bluecow.entity.Hero;
import com.bluecow.security.jwt.JwtProvider;
import com.bluecow.service.GameService;
import com.bluecow.service.HeroService;
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

    @Autowired
    JwtProvider jwtProvider;

    public HeroController(HeroService heroService) {
        this.heroService = heroService;
    }

    @GetMapping("/view")
    public ResponseEntity<Hero> viewGames(@RequestHeader("Authorization") String authReq,
                                          @RequestParam("hero") String hero) throws Exception {
        if (authReq != null && authReq.startsWith("Bearer ")) {
            authReq = authReq.replace("Bearer ", "");
            authReq = jwtProvider.getEmailFromToken(authReq);
            log.info("authreq=", authReq);
        }
        return new ResponseEntity<Hero>(heroService.viewHero(authReq, hero), HttpStatus.OK);
    }
}