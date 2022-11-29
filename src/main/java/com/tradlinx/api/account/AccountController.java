package com.tradlinx.api.account;

import com.tradlinx.api.account.form.*;
import com.tradlinx.api.jwt.JwtFilter;
import com.tradlinx.api.jwt.JwtToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = {"Account API"})
public class AccountController {

    private final AccountService accountService;
    private final AccountRepository accountRepository;

    @ApiOperation(value = "회원 가입", notes = "회원 가입 API")
    @PostMapping("/signup")
    public ResponseEntity<NewSignUpDto> SignUp(@RequestBody SignUpDto signUpDto) {
        String userid = accountService.processNewAccount(signUpDto);
        return new ResponseEntity<>(new NewSignUpDto(userid), HttpStatus.OK);
    }

    @ApiOperation(value = "로그인", notes = "로그인 API")
    @PostMapping("/signin")
    public ResponseEntity<JwtToken> signin(@RequestBody LoginDto loginDto) {
        accountRepository.findById(loginDto.getUserid())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 ID 입니다"));

        String jwt = accountService.processLogin(loginDto);
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
