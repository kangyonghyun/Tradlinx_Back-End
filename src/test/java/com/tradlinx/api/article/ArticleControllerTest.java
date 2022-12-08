package com.tradlinx.api.article;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradlinx.api.account.Account;
import com.tradlinx.api.account.AccountRepository;
import com.tradlinx.api.account.AccountService;
import com.tradlinx.api.account.dto.AccountLoginRequest;
import com.tradlinx.api.article.dto.ArticleUpdateRequest;
import com.tradlinx.api.account.dto.AccountSaveRequest;
import com.tradlinx.api.article.dto.ArticleWriteRequest;
import com.tradlinx.api.article.dto.CommentWriteRequest;
import com.tradlinx.api.comment.CommentRepository;
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

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    @Autowired
    CommentRepository commentRepository;

    AccountSaveRequest accountSaveRequest;
    ArticleWriteRequest articleDto;

    @BeforeEach
    void beforeEach() {
        accountSaveRequest = new AccountSaveRequest();
        accountSaveRequest.setUserId("userid");
        accountSaveRequest.setPw("passw0rd");
        accountSaveRequest.setUsername("username");
        accountService.processNewAccount(accountSaveRequest);

        accountSaveRequest = new AccountSaveRequest();
        accountSaveRequest.setUserId("userid2");
        accountSaveRequest.setPw("passw0rd");
        accountSaveRequest.setUsername("username");
        accountService.processNewAccount(accountSaveRequest);

        articleDto = new ArticleWriteRequest();
        articleDto.setArticleTitle("articleTitle");
        articleDto.setArticleContents("articleContents");
    }

    @AfterEach
    void afterEach() {
        articleRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @WithUserDetails(value = "userid", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("글 작성 - 포인트 증가 +3")
    void article_write_success() throws Exception {
        mockMvc.perform(post("/article")
                        .header(HttpHeaders.AUTHORIZATION, new StringStartsWith("Bearer "))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleDto)))
                .andExpect(status().isOk())
                .andExpect(authenticated())
                .andExpect(jsonPath("articleId", is(3)));

        Article article = articleRepository.findById(3L).orElseThrow();
        assertThat(article.getArticleTitle()).isEqualTo("articleTitle");
        assertThat(article.getArticleContents()).isEqualTo("articleContents");

        Account account = accountRepository.findByUserId("userid").orElseThrow();
        assertThat(account.getPoints()).isEqualTo(3);
    }

    @WithUserDetails(value = "userid", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("글 수정")
    void article_update_success() throws Exception {
        articleService.writeArticle(articleDto);

        ArticleUpdateRequest articleUpdateRequest = new ArticleUpdateRequest();
        articleUpdateRequest.setArticleId(2L);
        articleUpdateRequest.setArticleTitle("updateArticleTitle");
        articleUpdateRequest.setArticleContents("updateArticleContents");

        mockMvc.perform(put("/article")
                        .header(HttpHeaders.AUTHORIZATION, new StringStartsWith("Bearer "))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(authenticated())
                .andExpect(jsonPath("articleId", is(2)));

        Article article = articleRepository.findById(2L).orElseThrow();
        assertThat(article.getArticleTitle()).isEqualTo("updateArticleTitle");
        assertThat(article.getArticleContents()).isEqualTo("updateArticleContents");
    }

    @WithUserDetails(value = "userid", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("글 삭제 - 포인트 감소 -3")
    void article_delete_success() throws Exception {
        articleService.writeArticle(articleDto);

        Account account = accountRepository.findByUserId("userid").orElseThrow();
        assertThat(account.getPoints()).isEqualTo(3);

        mockMvc.perform(delete("/article/2")
                        .header(HttpHeaders.AUTHORIZATION, new StringStartsWith("Bearer ")))
                .andExpect(status().isOk())
                .andExpect(authenticated())
                .andExpect(jsonPath("count", is(1)));

        assertThat(articleRepository.findById(2L)).isEmpty();
        assertThat(account.getPoints()).isEqualTo(0);
    }

    @WithUserDetails(value = "userid", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("글 조회 - commentId 응답")
    void article_get_success() throws Exception {
        articleService.writeArticle(articleDto);

        mockMvc.perform(get("/article/2")
                        .header(HttpHeaders.AUTHORIZATION, new StringStartsWith("Bearer ")))
                .andExpect(status().isOk())
                .andExpect(authenticated())
                .andExpect(jsonPath("articleId", is(2)))
                .andExpect(jsonPath("commentsId", is(new ArrayList())));
    }

    @WithUserDetails(value = "userid", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("댓글 작성 - commentId 응답, 댓글 작성자 points +2, 원글 작성자 points +1")
    void comment_write_success() throws Exception {
        articleService.writeArticle(articleDto);

        anotherAccountAndLogin();

        CommentWriteRequest commentWriteRequest = new CommentWriteRequest();
        commentWriteRequest.setArticleId(2L);
        commentWriteRequest.setCommentContents("commentsContents");

        mockMvc.perform(post("/comments")
                        .header(HttpHeaders.AUTHORIZATION, new StringStartsWith("Bearer "))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentWriteRequest)))
                .andExpect(status().isOk())
                .andExpect(authenticated())
                .andExpect(jsonPath("commentId", is(4)));

        Article article = articleRepository.findById(2L).orElseThrow();
        assertThat(article.getAccount().getPoints()).isEqualTo(4);

        Account account = accountRepository.findByUserId("userid2").orElseThrow();
        assertThat(account.getPoints()).isEqualTo(2);
    }

    @WithUserDetails(value = "userid", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("댓글 삭제 - commentId 응답, 댓글 작성자 points -2, 원글 작성자 points -1")
    void comment_delete_success() throws Exception {
        articleService.writeArticle(articleDto);

        anotherAccountAndLogin();

        CommentWriteRequest commentWriteRequest = new CommentWriteRequest();
        commentWriteRequest.setArticleId(2L);
        commentWriteRequest.setCommentContents("commentsContents");
        articleService.writeComment(commentWriteRequest);

        mockMvc.perform(delete("/comments/4")
                        .header(HttpHeaders.AUTHORIZATION, new StringStartsWith("Bearer ")))
                .andExpect(status().isOk())
                .andExpect(authenticated())
                .andExpect(jsonPath("commentId", is(4)));

        Article article = articleRepository.findById(2L).orElseThrow();
        assertThat(article.getAccount().getPoints()).isEqualTo(3);

        Account account = accountRepository.findByUserId("userid2").orElseThrow();
        assertThat(account.getPoints()).isEqualTo(0);
    }

    private void anotherAccountAndLogin() {
        AccountLoginRequest accountLoginRequest = new AccountLoginRequest();
        accountLoginRequest.setUserId("userid2");
        accountLoginRequest.setPw("passw0rd");
        accountService.processLogin(accountLoginRequest);
    }

}