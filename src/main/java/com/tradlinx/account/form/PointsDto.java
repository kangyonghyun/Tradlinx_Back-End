package com.tradlinx.account.form;

import com.tradlinx.account.Account;
import lombok.Data;

@Data
public class PointsDto {
    private int points;

    public PointsDto(int points) {
        this.points = points;
    }

    public static PointsDto from(Account account) {
        if(account == null) {
            return null;
        }
        return new PointsDto(account.getPoints());
    }
}
