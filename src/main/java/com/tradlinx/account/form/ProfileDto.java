package com.tradlinx.account.form;

import com.tradlinx.account.Account;
import lombok.Builder;
import lombok.Data;

@Data @Builder
public class ProfileDto {
    private String userid;
    private String username;

    public static ProfileDto from(Account account) {
        if(account == null) {
            return null;
        }
        return ProfileDto.builder()
                .userid(account.getUserid())
                .username(account.getUsername())
                .build();
    }
}
