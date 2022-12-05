package com.tradlinx.api.account.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AccountLoginRequest {

    @NotBlank
    private String userId;
    @NotBlank
    private String pw;

}
