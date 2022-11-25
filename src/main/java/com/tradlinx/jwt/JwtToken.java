package com.tradlinx.jwt;

import lombok.Data;

@Data
public class JwtToken {
    String token;

    public JwtToken(String token) {
        this.token = token;
    }
}
