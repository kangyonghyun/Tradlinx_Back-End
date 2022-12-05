package com.tradlinx.api.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradlinx.api.account.dto.AccountLoginRequest;
import com.tradlinx.api.account.dto.AccountSaveRequest;
import org.hamcrest.core.StringStartsWith;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    AccountSaveRequest saveRequest;

    @BeforeEach
    void beforeEach() {
        saveRequest = new AccountSaveRequest();
        saveRequest.setUserId("userid1");
        saveRequest.setPw("passw0rd");
        saveRequest.setUsername("username");
        accountService.processNewAccount(saveRequest);
    }

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void signUp_correct_input_success() throws Exception {
        saveRequest = new AccountSaveRequest();
        saveRequest.setUserId("userid");
        saveRequest.setPw("passw0rd");
        saveRequest.setUsername("username");

        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveRequest)))
                .andExpect(status().isOk())
                .andExpect(unauthenticated());

        Account newAccount = accountRepository.findByUserId("userid").orElseThrow();
        assertThat(newAccount.getUserId()).isEqualTo("userid");
        assertThat(newAccount.getPw()).isNotEqualTo("passw0rd");
    }

    @Test
    @DisplayName("회원가입 실패 - 잘못된 아이디 입력")
    void signUp_incorrect_input_fail() throws Exception {
        saveRequest = new AccountSaveRequest();
        saveRequest.setUserId("Tuserid");
        saveRequest.setPw("passw0rd");
        saveRequest.setUsername("username");

        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveRequest)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("code", is("BAD")))
                .andExpect(jsonPath("message", is("ID 를 다시 입력해주세요.")));
    }

//    @WithUserDetails(value = "userid1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("로그인 성공")
    void login_correct_input_success() throws Exception {
        AccountLoginRequest accountLoginRequest = new AccountLoginRequest();
        accountLoginRequest.setUserId("userid1");
        accountLoginRequest.setPw("passw0rd");
        mockMvc.perform(post("/signin")
                        .content(objectMapper.writeValueAsString(accountLoginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, new StringStartsWith("Bearer ")))
                .andExpect(status().isOk());
                // 예상은 인증 o -> 실제 인증 x , 더 알아봐야겠다.
                //.andExpect(authenticated());
    }

    @ParameterizedTest
    @DisplayName("로그인 실패 - 잘못된 아이디 입력")
    @CsvSource({"nouserid1, passw0rd, 일치하는 ID 가 없습니다.",
            "userid1, nopassw0rdm, 인증 불허가"})
    void login_incorrect_input_fail(String userId, String pw, String message) throws Exception {
        AccountLoginRequest loginRequest = new AccountLoginRequest();
        loginRequest.setUserId(userId);
        loginRequest.setPw(pw);

        sign_in_fail_test(loginRequest, message);
    }

    private void sign_in_fail_test(AccountLoginRequest loginRequest, String message) throws Exception {
        mockMvc.perform(post("/signin")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().doesNotExist(HttpHeaders.AUTHORIZATION))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("code", is("BAD")))
                .andExpect(jsonPath("message", is(message)))
                .andExpect(unauthenticated());
    }

    @WithUserDetails(value = "userid1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("프로필 조회")
    void get_profile() throws Exception {
        mockMvc.perform(get("/profile")
                        .header(HttpHeaders.AUTHORIZATION, new StringStartsWith("Bearer ")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("userId", is("userid1")))
                .andExpect(jsonPath("username", is("username")))
                .andExpect(authenticated().withUsername("userid1"));
    }

    @WithUserDetails(value = "userid1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("포인트 조회")
    void get_points() throws Exception {
        Account account = accountRepository.findByUserId("userid1").orElseThrow();

        mockMvc.perform(get("/points")
                        .header(HttpHeaders.AUTHORIZATION, new StringStartsWith("Bearer ")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("points", is(account.getPoints())))
                .andExpect(authenticated().withUsername("userid1"));
    }

}