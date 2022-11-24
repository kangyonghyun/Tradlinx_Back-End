package com.tradlinx.account;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter @Setter
public class Account {

    @Id
    String userid;
    String pw;
    String username;

}
