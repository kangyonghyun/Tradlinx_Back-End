package com.tradlinx.api.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUserId(String userId);
    boolean existsByUserId(String userId);
    Account findByUserIdAndPw(String userid, String pw);
    Optional<Account> findOneWithByUserId(String userId);

}
