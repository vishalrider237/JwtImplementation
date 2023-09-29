package com.jwtuthentication.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class JWTResponse {
    private String JwtToken;
    private String refreshToken;
    private String username;
}
