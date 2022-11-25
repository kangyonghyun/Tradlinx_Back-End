package com.tradlinx.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradlinx.account.form.LoginDto;
import com.tradlinx.account.form.SignUpDto;
import com.tradlinx.jwt.JwtTokenProvider;
import org.hamcrest.core.StringStartsWith;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    @Autowired
    JwtTokenProvider tokenProvider;

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
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, new StringStartsWith("Bearer ")))
                .andDo(print());
    }

}