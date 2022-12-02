package com.tradlinx.api.account.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SignInDto {
    @NotBlank
    private String userId;
    @NotBlank
    private String pw;
}
