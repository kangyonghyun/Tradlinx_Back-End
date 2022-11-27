package com.tradlinx.article;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradlinx.account.Account;
import com.tradlinx.account.AccountRepository;
import com.tradlinx.account.AccountService;
import com.tradlinx.account.form.ArticleUpdateDto;
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
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
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
    @Autowired
    ArticleService articleService;

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
        articleRepository.deleteAll();
    }

    @WithUserDetails(value = "userid", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("글 작성 - 포인트 증가 +3")
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
        assertThat(article.getArticleTitle()).isEqualTo("articleTitle");
        assertThat(article.getArticleContents()).isEqualTo("articleContents");

        Account account = accountRepository.findById("userid").orElseThrow();
        assertThat(account.getPoints()).isEqualTo(3);
    }

    @WithUserDetails(value = "userid", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("글 수정")
    void article_update_success() throws Exception {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setArticleTitle("articleTitle");
        articleDto.setArticleContents("articleContents");
        articleService.writeArticle(articleDto);

        ArticleUpdateDto articleUpdateDto = new ArticleUpdateDto();
        articleUpdateDto.setArticleId("articleId");
        articleUpdateDto.setArticleTitle("updateArticleTitle");
        articleUpdateDto.setArticleContents("updateArticleContents");

        mockMvc.perform(put("/article")
                        .header(HttpHeaders.AUTHORIZATION, new StringStartsWith("Bearer "))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(authenticated())
                .andExpect(content().string("articleId"));

        Article article = articleRepository.findById("articleId").orElseThrow();
        assertThat(article.getArticleTitle()).isEqualTo("updateArticleTitle");
        assertThat(article.getArticleContents()).isEqualTo("updateArticleContents");
    }

}