package com.tradlinx.article;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradlinx.account.AccountRepository;
import com.tradlinx.account.AccountService;
import com.tradlinx.account.form.SignUpDto;
import com.tradlinx.article.form.ArticleDto;
import org.assertj.core.api.Assertions;
import org.hamcrest.core.StringStartsWith;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ArticleControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AccountService accountService;
    @Autowired
    ArticleRepository articleRepository;

    SignUpDto signUpDto;
    @BeforeEach
    void beforeEach() {
        signUpDto = new SignUpDto();
        signUpDto.setUserid("userid");
        signUpDto.setPw("passw0rd");
        signUpDto.setUsername("username");
        accountService.processNewAccount(signUpDto);
    }

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }

    @WithUserDetails(value = "userid", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("글 작성 - 성공")
    void article_write_success() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setArticleTitle("articleTitle");
        articleDto.setArticleContents("articleContents");

        mockMvc.perform(post("/article")
                        .header(HttpHeaders.AUTHORIZATION, new StringStartsWith("Bearer "))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleDto)))
                .andExpect(status().isOk())
                .andExpect(authenticated())
                .andExpect(content().string("articleId"));

        Article article = articleRepository.findById("articleId").orElseThrow();
        Assertions.assertThat(article.getArticleTitle()).isEqualTo("articleTitle");
        Assertions.assertThat(article.getArticleContents()).isEqualTo("articleContents");
    }

}