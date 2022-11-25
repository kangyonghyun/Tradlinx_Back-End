package com.tradlinx.account;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Account {

    @Id
    private String userid;
    private String pw;
    private String username;
    private int points;

    public Account(String userid, String pw) {
        this.userid = userid;
        this.pw = pw;
    }
}
