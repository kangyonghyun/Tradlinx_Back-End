package com.tradlinx.api.account.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class AccountProfileRequest {

    private String userId;
    private String username;

}
