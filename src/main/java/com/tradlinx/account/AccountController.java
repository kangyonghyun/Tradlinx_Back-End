package com.tradlinx.account;

import com.tradlinx.account.form.LoginForm;
import com.tradlinx.account.form.NewSignUpDto;
import com.tradlinx.account.form.SignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/signup")
    public ResponseEntity<String> SignUp(@RequestBody SignUpDto signUpDto) {
        String userid = accountService.processNewAccount(signUpDto);
        return new ResponseEntity<>(userid + "님 가입 완료했습니다.", HttpStatus.OK);
    }

}
