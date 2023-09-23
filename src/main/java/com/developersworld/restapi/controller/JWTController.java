package com.developersworld.restapi.controller;

import com.developersworld.restapi.entities.JWTRequest;
import com.developersworld.restapi.entities.JWTResponse;
import com.developersworld.restapi.exception.UserNotFoundException;
import com.developersworld.restapi.helper.JWTService;
import com.developersworld.restapi.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JWTController {

    @Autowired
    JWTService jwtHelper;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/token")
    public ResponseEntity<JWTResponse> getToken(@RequestBody JWTRequest jwtRequest){
        System.out.println(jwtRequest);
        try {
            Authentication authentication =      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername()
                    , jwtRequest.getPassword()));
            if (authentication.isAuthenticated()) {
                return ResponseEntity.ok(new JWTResponse(jwtHelper.generateToken(jwtRequest.getUsername())));
            } else {
                throw new UsernameNotFoundException("user is not found sorry !!");
            }
        }
        catch (Exception ex){
            throw new UserNotFoundException("user name not found");
        }
    }
}
