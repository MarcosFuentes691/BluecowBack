package com.bluecow.utility;

import com.bluecow.security.jwt.JwtProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BearerCleaner {

    @Autowired
    JwtProvider jwtProvider;

    public String cleanBearer(String authReq) {
        try {
            if (authReq != null && authReq.startsWith("Bearer ")) {
                authReq = authReq.replace("Bearer ", "");
                authReq = jwtProvider.getEmailFromToken(authReq);
            }
        } catch (Exception e) {
            log.warn("exception on token, controller, bearerclaner");
        }
        return authReq;
    }

}