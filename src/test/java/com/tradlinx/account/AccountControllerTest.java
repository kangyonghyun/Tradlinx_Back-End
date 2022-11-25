package com.tradlinx.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradlinx.account.form.LoginDto;
import com.tradlinx.account.form.SignUpDto;
import org.hamcrest.core.StringStartsWith;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AccountService accountService;

    @Test
    @DisplayName("회원 가입 - 성공")
    void signUp_correct_input() throws Exception {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setUserid("userid");
        signUpDto.setPw("passw0rd");
        signUpDto.setUsername("username");

        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpDto)))
                .andExpect(status().isOk());

        Account newAccount = accountRepository.findById("userid").get();
        assertThat(newAccount.getUserid()).isEqualTo("userid");
    }
    @Test
    @DisplayName("로그인 - 성공")
    void login_success() throws Exception {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setUserid("userid");
        signUpDto.setPw("passw0rd");
        signUpDto.setUsername("username");
        accountService.processNewAccount(signUpDto);

        LoginDto loginDto = new LoginDto();
        loginDto.setUserid("userid");
        loginDto.setPw("passw0rd");

        mockMvc.perform(post("/signin")
                        .content(objectMapper.writeValueAsString(loginDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, new StringStartsWith("Bearer ")))
                .andDo(print());
    }

    @Test
    @DisplayName("프로필 조회")
    void get_profile() throws Exception {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setUserid("userid");
        signUpDto.setPw("passw0rd");
        signUpDto.setUsername("username");
        accountService.processNewAccount(signUpDto);

        LoginDto loginDto = new LoginDto();
        loginDto.setUserid("userid");
        loginDto.setPw("passw0rd");
        accountService.processLogin(loginDto);

        mockMvc.perform(get("/profile")
                        .header(HttpHeaders.AUTHORIZATION, new StringStartsWith("Bearer ")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("userid", is("userid")))
                .andExpect(jsonPath("username", is("username")));
    }

    @Test
    @DisplayName("포인트 조회")
    void get_points() throws Exception {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setUserid("userid");
        signUpDto.setPw("passw0rd");
        signUpDto.setUsername("username");
        accountService.processNewAccount(signUpDto);

        LoginDto loginDto = new LoginDto();
        loginDto.setUserid("userid");
        loginDto.setPw("passw0rd");
        accountService.processLogin(loginDto);

        mockMvc.perform(get("/points")
                        .header(HttpHeaders.AUTHORIZATION, new StringStartsWith("Bearer ")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("points", is(0)));
    }

}