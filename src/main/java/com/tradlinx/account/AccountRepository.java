package com.tradlinx.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {
    Account findByUseridAndPw(String userid, String pw);
    Optional<Account> findOneWithByUserid(String userid);
}
