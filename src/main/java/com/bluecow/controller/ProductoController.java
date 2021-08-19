package com.bluecow.controller;

import com.bluecow.entity.Producto;
import com.bluecow.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/producto")
@CrossOrigin
public class ProductoController {

    @Autowired
    JwtProvider jwtProvider;



    @GetMapping("/lista")
    public ResponseEntity<List<Producto>> lista(@RequestHeader("Authorization") String authReq){
        //String authReq = req.getHeader("Authorization");
        String email="hola";
        if(authReq != null && authReq.startsWith("Bearer ")) {
            email = authReq.replace("Bearer ", "");
        }
        email = jwtProvider.getEmailFromToken(email);
        String finalEmail = email;
        ArrayList productos = new ArrayList(){{
            add(new Producto(finalEmail, 20));
            add(new Producto(authReq, 30));
            add(new Producto(authReq, 40));
        }};
        return new ResponseEntity<List<Producto>>(productos, HttpStatus.OK);
    }

}
