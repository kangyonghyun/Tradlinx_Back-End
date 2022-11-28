package com.tradlinx.account.form;

import lombok.Data;

@Data
public class NewSignUpDto {
    private String userid;

    public NewSignUpDto(String userid) {
        this.userid = userid;
    }
}
