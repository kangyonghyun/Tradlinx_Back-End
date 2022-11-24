package com.tradlinx.account;

import lombok.Data;

@Data
public class AccessToken {
    private String token;

    public AccessToken(String token) {
        this.token = token;
    }
}
