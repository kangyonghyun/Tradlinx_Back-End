package com.tradlinx.account;

import com.tradlinx.account.form.NewAccountForm;
import com.tradlinx.account.form.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/signup")
    public NewAccountForm SignUp(@RequestBody SignUpForm signUpForm) {
        return accountService.processNewAccount(signUpForm);
    }

}
