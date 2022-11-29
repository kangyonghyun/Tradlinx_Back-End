package com.tradlinx.api.jwt;

import lombok.Data;

@Data
public class JwtToken {
    String token;

    public JwtToken(String token) {
        this.token = "Bearer " + token;
    }
}
