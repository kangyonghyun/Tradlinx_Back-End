package com.tradlinx.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradlinx.account.form.SignUpDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

}