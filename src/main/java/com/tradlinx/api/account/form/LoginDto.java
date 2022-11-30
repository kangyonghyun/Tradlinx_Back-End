package com.tradlinx.api.account.form;

import lombok.Data;

@Data
public class LoginDto {
    private String userId;
    private String pw;
}
