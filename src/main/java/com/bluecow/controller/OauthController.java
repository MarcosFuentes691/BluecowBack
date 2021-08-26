package com.bluecow.controller;

import com.bluecow.dto.TokenDto;
import com.bluecow.entity.Player;
import com.bluecow.entity.Role;
import com.bluecow.consts.roleName;
import com.bluecow.security.jwt.JwtProvider;
import com.bluecow.service.PlayerService;
import com.bluecow.service.RoleService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/oauth")
@CrossOrigin
public class OauthController {

    @Value("${google.clientId}")
    String googleClientId;

    @Value("${secretPsw}")
    String secretPsw;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    PlayerService playerService;

    @Autowired
    RoleService roleService;


    @PostMapping("/user")
    public ResponseEntity<TokenDto> user(@RequestParam String username, @RequestParam String password) throws IOException {
        Player player;
        if(playerService.existsEmail(username))
            player = playerService.getByEmail(username).get();
        else
            player = savePlayer(username,username,password);
        TokenDto tokenRes = login(player,password);
        return new ResponseEntity(tokenRes, HttpStatus.OK);
    }

    @PostMapping("/google")
    public ResponseEntity<TokenDto> google(@RequestBody TokenDto tokenDto) throws IOException {
        final NetHttpTransport transport = new NetHttpTransport();
        final JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
        GoogleIdTokenVerifier.Builder verifier =
                new GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
                .setAudience(Collections.singletonList(googleClientId));
        final GoogleIdToken googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(), tokenDto.getValue());
        final GoogleIdToken.Payload payload = googleIdToken.getPayload();
        Player player;
        if(playerService.existsEmail(payload.getEmail()))
            player = playerService.getByEmail(payload.getEmail()).get();
        else
            player = savePlayer(payload.getEmail(),(String) payload.get("name"),secretPsw);
        TokenDto tokenRes = login(player,secretPsw);
        return new ResponseEntity(tokenRes, HttpStatus.OK);
    }

    private TokenDto login(Player player,String password){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(player.getEmail(), password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        if(!Objects.equals(password, secretPsw))
            jwt="Bearer ".concat(jwt);
        TokenDto tokenDto = new TokenDto();
        tokenDto.setValue(jwt);
        return tokenDto;
    }

    private Player savePlayer(String email,String name,String password){
        Player player = new Player(email, passwordEncoder.encode(password),name, Calendar.getInstance());
        Role roleUser = roleService.getByNameRole(roleName.ROLE_USER).get();
        Set<Role> roles = new HashSet<>();
        roles.add(roleUser);
        player.setRoles(roles);
        return playerService.save(player);
    }

}