package com.bluecow.controller;

import com.bluecow.entity.Game;
import com.bluecow.entity.Hero;
import com.bluecow.repository.GameRepository;
import com.bluecow.security.jwt.JwtProvider;
import com.bluecow.service.HeroService;
import com.bluecow.utility.BearerCleaner;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/hero")
@CrossOrigin
@Slf4j
public class HeroController {

    private final HeroService heroService;

    @Autowired
    BearerCleaner bearerCleaner;

    public HeroController(HeroService heroService) {
        this.heroService = heroService;
    }

    @ApiOperation(value = "View all heroes of the user")
    @GetMapping("/all")
    public ResponseEntity<?> viewHeroes(@RequestHeader("Authorization") String authReq,
                                        @RequestParam(required=false) String from,
                                        @RequestParam(required=false) String to,
                                        @RequestParam(required=false) String timeZone){
        authReq=bearerCleaner.cleanBearer(authReq);
        Collection<Hero> heroes;
        try {
            heroes=heroService.viewHeroes(authReq,from,to,timeZone);
        }
        catch(Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.status(200).body(heroes);
    }

    @ApiOperation(value = "Search of heroes of the user")
    @GetMapping("/search")
    public ResponseEntity<?> searchHero(@RequestHeader("Authorization") String authReq,
                                         @RequestParam(required=false) String hero,
                                         @RequestParam(required=false) String from,
                                         @RequestParam(required=false) String to,
                                        @RequestParam(required=false) String timeZone
                                        ){
        authReq=bearerCleaner.cleanBearer(authReq);
        try {
            return new ResponseEntity<>(heroService.searchHero(authReq, hero, from, to,timeZone), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}