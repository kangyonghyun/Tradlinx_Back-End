package com.tradlinx.api.account;

import com.tradlinx.api.account.form.*;
import com.tradlinx.api.account.validator.SignInValidator;
import com.tradlinx.api.account.validator.SignUpValidator;
import com.tradlinx.api.jwt.JwtFilter;
import com.tradlinx.api.jwt.JwtToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Api(tags = {"Account API"})
public class AccountController {

    private final AccountService accountService;
    private final AccountRepository accountRepository;

    private final SignUpValidator signUpValidator;
    private final SignInValidator signInValidator;

    @InitBinder("signUpDto")
    public void initBinder1(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpValidator);
    }

    @InitBinder("signInDto")
    public void initBinder2(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signInValidator);
    }

    @ApiOperation(value = "회원 가입", notes = "회원 가입 API")
    @PostMapping("/signup")
    public NewSignUpDto signUp(@RequestBody @Valid SignUpDto signUpDto) {
        String userId = accountService.processNewAccount(signUpDto);
        return new NewSignUpDto(userId);
    }

    @ApiOperation(value = "로그인", notes = "로그인 API")
    @PostMapping("/signin")
    public ResponseEntity<JwtToken> signin(@RequestBody @Valid SignInDto signInDto) {
        String jwt = accountService.processLogin(signInDto);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JwtToken(jwt), httpHeaders, HttpStatus.OK);
    }

    @ApiOperation(value = "회원 조회", notes = "회원 조회 API")
    @GetMapping("/profile")
    public ResponseEntity<ProfileDto> profile() {
        ProfileDto profile = accountService.getProfile();
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }

    @ApiOperation(value = "포인트 조회", notes = "포인트 조회 API")
    @GetMapping("/points")
    public ResponseEntity<PointsDto> points() {
        PointsDto points = accountService.getPoints();
        return new ResponseEntity<>(points, HttpStatus.OK);
    }


}
