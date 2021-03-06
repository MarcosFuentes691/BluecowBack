package com.bluecow.security.jwt;

import com.bluecow.security.UserAuth;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Component
public class JwtProvider {

    private final static Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${jwt.secret}")
    String secret;

    @Value("${jwt.expiration}")
    int expiration;

    public String generateToken(Authentication authentication){
        UserAuth userAuth = (UserAuth) authentication.getPrincipal();
        return Jwts.builder().setSubject(userAuth.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                //.setExpiration(java.util.Date.from(LocalDate.of(2022,1,1).atStartOfDay().toInstant(ZoneOffset.UTC)))//(System.currentTimeMillis() + expiration))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String getEmailFromToken(String token){
        //return "marcosfuentes691@gmail.com";
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        }catch (MalformedJwtException | UnsupportedJwtException | ExpiredJwtException | IllegalArgumentException | SignatureException e){
            logger.error("error comprobando el token");
            e.printStackTrace();
        }
        return false;
    }


}
