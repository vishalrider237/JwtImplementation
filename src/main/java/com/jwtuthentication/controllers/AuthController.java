package com.jwtuthentication.controllers;

import com.jwtuthentication.entity.RefreshToken;
import com.jwtuthentication.entity.User;
import com.jwtuthentication.models.JWTRequest;
import com.jwtuthentication.models.JWTResponse;
import com.jwtuthentication.models.RefreshTokenRequest;
import com.jwtuthentication.security.JWTHelper;
import com.jwtuthentication.service.RefreshTokenService;
import com.jwtuthentication.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager manager;


    @Autowired
    private JWTHelper helper;
    @Autowired
    private UserService userService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    private Logger logger = LoggerFactory.getLogger(AuthController.class);


    @PostMapping("/login")
    public ResponseEntity<JWTResponse> login(@RequestBody JWTRequest request) {

        this.doAuthenticate(request.getEmail(), request.getPassword());


        UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.helper.generateToken(userDetails);
        RefreshToken refreshToken = this.refreshTokenService.createRefreshToken(userDetails.getUsername());

        JWTResponse response = JWTResponse.builder()
                .JwtToken(token)
                .refreshToken(refreshToken.getRefreshToken())
                .username(userDetails.getUsername()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void doAuthenticate(String email, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            manager.authenticate(authentication);


        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }

    }

    // generating JWT token from our old refresh token
    @PostMapping("/refresh")
    public  JWTResponse RereshJwtToken(@RequestBody RefreshTokenRequest request){
      RefreshToken refreshToken=  this.refreshTokenService.verifyRefreshToken(request.getRefreshToken());
   User user= refreshToken.getUser();
      String token=   this.helper.generateToken(user);
      return JWTResponse.builder().refreshToken(refreshToken.getRefreshToken())
              .username(user.getUsername())
              .JwtToken(token).build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "Credentials Invalid !!";
    }
    @PostMapping("/create-user")
    public User createUser(@RequestBody com.jwtuthentication.entity.User user){
        return this.userService.createUser(user);
    }

}
