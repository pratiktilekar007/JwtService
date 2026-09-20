package com.lcwd.jwt.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AuthResponse {

    private String token;
    private String tokenType;
    private long expiresIn;
    private String username;


}
