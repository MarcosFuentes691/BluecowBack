package com.bluecow.controller;

import com.bluecow.dto.TokenDto;
import com.bluecow.entity.Player;
import com.bluecow.entity.Role;
import com.bluecow.consts.roleName;
import com.bluecow.security.jwt.JwtProvider;
import com.bluecow.service.PlayerService;
import com.bluecow.service.RoleService;
import com.bluecow.utility.BearerCleaner;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/oauth")
@CrossOrigin
@Slf4j
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

    private static class LoginForm {
        public String getUsername() {
            return username;
        }
        public String getPassword() {
            return password;
        }
        private String username;
        private String password;
    }

    private static class RegisterForm {
        public String getUsername() {
            return username;
        }
        public String getPassword() {
            return password;
        }
        public String getName() {
            return name;
        }
        private String name;
        private String username;
        private String password;
    }

    @ApiOperation(value = "Register a new user")
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterForm registerForm) {
        String username=registerForm.getUsername();
        String password=registerForm.getPassword();
        String name=registerForm.getName();
        Player player;
        if(playerService.existsEmail(username))
            return new ResponseEntity<>("Already exists", HttpStatus.FORBIDDEN);
        else
            player = savePlayer(username,name,password);
        return new ResponseEntity<>("Created succesfully", HttpStatus.OK);
    }

    @ApiOperation(value = "Login with a user (not Google user)")
    @PostMapping("/user")
    public ResponseEntity<TokenDto> user(@RequestBody LoginForm loginForm) throws Exception {
        String username=loginForm.getUsername();
        String password=loginForm.getPassword();
        Player player;
        if(playerService.existsEmail(username))
            player = playerService.getByEmail(username).get();
        else
            throw new Exception("Player doesnt exists");
        TokenDto tokenRes = login(player,password);
        return new ResponseEntity(tokenRes, HttpStatus.OK);
    }

    @ApiOperation(value = "Login with a google account")
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

    @ApiIgnore
    @GetMapping("/check")
    public ResponseEntity<UserResponse> user (@RequestHeader("Authorization") String authReq) {
        int i=0;
        BearerCleaner bearerCleaner = new BearerCleaner();
        authReq=bearerCleaner.cleanBearer(authReq);
        Player player=playerService.getByEmail(authReq).get();
        UserResponse userResponse;
        if(player.getEmail().equals(player.getName()))
            userResponse = new UserResponse(authReq);
        else
            userResponse = new UserResponse();
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    private TokenDto login(Player player,String password){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(player.getEmail(), password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
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

    private static class UserResponse{
        boolean valid;
        String name;
        String email;
        /*String authToken;
        String id;
        String provider;
        String authorizationCode;
        String firstName;
        String idToken;
        String lastName;
        String photoUrl;*/

        public UserResponse(String name) {
            this.valid = true;
            this.name = name;
            this.email = name;
        }

        public UserResponse() {
            this.valid = false;
        }

        public boolean isValid() {
            return valid;
        }

        public void setValid(boolean valid) {
            this.valid = valid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}