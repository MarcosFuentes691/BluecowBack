package com.bluecow.utility;

import com.bluecow.security.jwt.JwtProvider;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.bluecow.consts.ConfigConstantsLearnAboutEnvironmentPls.*;

import static com.bluecow.consts.ConfigConstantsLearnAboutEnvironmentPls.secret;

@Slf4j
@Component
public class BearerCleaner {

    JwtProvider jwtProvider=new JwtProvider();

    public String cleanBearer(String authReq) {
        try {
            if (authReq != null && authReq.startsWith("Bearer ")) {
                authReq = authReq.replace("Bearer ", "");
                authReq = Jwts.parser().setSigningKey(secret).parseClaimsJws(authReq).getBody().getSubject();
            }
            else if (authReq != null) {
                authReq = Jwts.parser().setSigningKey(secret).parseClaimsJws(authReq).getBody().getSubject();
            }
        } catch (Exception e) {
            log.warn("exception on token, controller, bearerclaner");
        }
        return authReq;
    }

}