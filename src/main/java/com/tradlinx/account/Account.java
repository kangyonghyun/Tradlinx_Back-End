package com.tradlinx.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Account {

    @Id
    @Column(name = "user_id")
    private String userid;
    private String pw;
    private String username;
    private int points;

    public void addPoints() {
        this.points += 3;
    }

    public void minusPoints() {
        this.points -= 3;
    }

    public void addCommentPoints() {
        this.points += 2;
    }

    public void addWriterPoints() {
        this.points += 1;
    }
}
