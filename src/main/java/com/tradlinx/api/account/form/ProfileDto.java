package com.tradlinx.api.account.form;

import com.tradlinx.api.account.Account;
import lombok.Builder;
import lombok.Data;

@Data @Builder
public class ProfileDto {
    private String userId;
    private String username;

    public static ProfileDto from(Account account) {
        if(account == null) {
            return null;
        }
        return ProfileDto.builder()
                .userId(account.getUserId())
                .username(account.getUsername())
                .build();
    }
}
