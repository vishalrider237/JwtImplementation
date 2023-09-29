package com.jwtuthentication.models;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String refreshToken;
}
