package com.tradlinx.account;

import com.tradlinx.account.form.*;
import com.tradlinx.jwt.JwtFilter;
import com.tradlinx.jwt.JwtToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final AccountRepository accountRepository;

    @PostMapping("/signup")
    public ResponseEntity<NewSignUpDto> SignUp(@RequestBody SignUpDto signUpDto) {
        String userid = accountService.processNewAccount(signUpDto);
        return new ResponseEntity<>(new NewSignUpDto(userid), HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtToken> signin(@RequestBody LoginDto loginDto) {
        accountRepository.findById(loginDto.getUserid())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 ID 입니다"));

        String jwt = accountService.processLogin(loginDto);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JwtToken(jwt), httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileDto> profile() {
        ProfileDto profile = accountService.getProfile();
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }

    @GetMapping("/points")
    public ResponseEntity<PointsDto> points() {
        PointsDto points = accountService.getPoints();
        return new ResponseEntity<>(points, HttpStatus.OK);
    }


}
