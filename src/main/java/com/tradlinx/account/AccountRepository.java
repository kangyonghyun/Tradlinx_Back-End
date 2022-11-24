package com.tradlinx.account;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
    Account findByUseridAndPw(String userid, String pw);
}
