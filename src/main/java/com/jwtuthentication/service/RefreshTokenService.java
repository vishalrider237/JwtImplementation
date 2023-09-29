package com.jwtuthentication.service;

import com.jwtuthentication.entity.RefreshToken;
import com.jwtuthentication.entity.User;
import com.jwtuthentication.repositories.RefreshTokenRepository;
import com.jwtuthentication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {
    public long refreshTokenValidity=2*60*1000;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;

    public RefreshToken createRefreshToken(String username){
        User user=this.userRepository.findByEmail(username).get();
        RefreshToken refreshToken1=user.getRefreshToken();
        if (refreshToken1==null){
            refreshToken1=    RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expiry(Instant.now().plusMillis(refreshTokenValidity))
                    .user(this.userRepository.findByEmail(username).get())
                    .build();
        }
        else{
           refreshToken1.setExpiry(Instant.now().plusMillis(refreshTokenValidity));
        }
         user.setRefreshToken(refreshToken1);
    // save to database
    this.refreshTokenRepository.save(refreshToken1);
        return refreshToken1;
    }
    public RefreshToken verifyRefreshToken(String refreshToken) {
        RefreshToken refreshTokenOb = refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new RuntimeException("Refresh token not found"));
        if (refreshTokenOb.getExpiry().compareTo(Instant.now()) < 0) {
            this.refreshTokenRepository.delete(refreshTokenOb);
            throw new RuntimeException("Refresh Token expired!!");
        }
        return refreshTokenOb;
    }
}
