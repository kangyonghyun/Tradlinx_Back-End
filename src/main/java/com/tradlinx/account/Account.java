package com.tradlinx.account;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = "userid")
@AllArgsConstructor @NoArgsConstructor
public class Account {

    @Id @GeneratedValue
    String userid;
    String pw;
    String username;

}
