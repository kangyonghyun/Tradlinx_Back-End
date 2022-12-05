package com.tradlinx.api.account;

import com.tradlinx.api.account.dto.*;
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
    private final SignUpValidator signUpValidator;
    private final SignInValidator signInValidator;

    @InitBinder("accountSaveRequest")
    public void initBinder1(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpValidator);
    }

    @InitBinder("accountLoginRequest")
    public void initBinder2(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signInValidator);
    }

    @ApiOperation(value = "회원 가입", notes = "회원 가입 API")
    @PostMapping("/signup")
    public AccountSaveResponse signUp(@RequestBody @Valid AccountSaveRequest saveRequest) {
        String userId = accountService.processNewAccount(saveRequest);
        return new AccountSaveResponse(userId);
    }

    @ApiOperation(value = "로그인", notes = "로그인 API")
    @PostMapping("/signin")
    public ResponseEntity<JwtToken> signin(@RequestBody @Valid AccountLoginRequest loginRequest) {
        String jwt = accountService.processLogin(loginRequest);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JwtToken(jwt), httpHeaders, HttpStatus.OK);
    }

    @ApiOperation(value = "회원 조회", notes = "회원 조회 API")
    @GetMapping("/profile")
    public AccountProfileRequest profile() {
        return accountService.getProfile();
    }

    @ApiOperation(value = "포인트 조회", notes = "포인트 조회 API")
    @GetMapping("/points")
    public AccountPointsRequest points() {
        return accountService.getPoints();
    }


}
