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

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    public ResponseEntity<?> viewStats(@RequestHeader("Authorization") String authReq){
        authReq=bearerCleaner.cleanBearer(authReq);
        try {
            return new ResponseEntity<>(playerService.getPlayerStats(authReq),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/date")
    public ResponseEntity<?> viewDate(@RequestHeader("Authorization") String authReq,
                                      @RequestParam(required=false) String from,
                                      @RequestParam(required=false) String to,
                                      @RequestParam(required=false) String timeZone) throws Exception {
        authReq=bearerCleaner.cleanBearer(authReq);
        int timeZoneInt=0;
        if(!(to.equals("now"))){
            timeZone=timeZone.substring(0,3);
            timeZoneInt=Integer.parseInt(timeZone);
            timeZoneInt=60*(-timeZoneInt);
        }else
            timeZoneInt=Integer.parseInt(timeZone);
        Calendar calFrom = Calendar.getInstance();
        Calendar calTo = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        if(from.equals("Last week"))
            calFrom.add(Calendar.DATE,-6);
        else if(from.equals("Last month"))
            calFrom.add(Calendar.MONTH,-1);
        else if(from.equals("Always"))
            calFrom.add(Calendar.YEAR,-20);
        else if(!(from.equals("Today")))
            calFrom.setTime(sdf.parse(from));
        if(!(to.equals("now")))
            calTo.setTime(sdf.parse(to));
        calFrom.set(Calendar.HOUR_OF_DAY,0);
        calFrom.set(Calendar.MINUTE,0);
        calFrom.add(Calendar.MINUTE,timeZoneInt);
        calTo.add(Calendar.MINUTE,timeZoneInt);
        calTo.add(Calendar.DATE,1);//because it comes the same date from frontend
        if (calTo.getTime().before(calFrom.getTime()))
            throw new Exception("Invalid dates");
        try {
            return new ResponseEntity<>(playerService.makeStats(authReq,calFrom,calTo,"date",null),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
