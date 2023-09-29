package com.jwtuthentication.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Data
@Table(name = "refresh_tokens")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tokenId; // it is used for updating the refreshtoken
    private String refreshToken;// this refresh token ca be update also
    private Instant expiry;
    @OneToOne
    private User user;

}
