package com.tradlinx.api.article;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradlinx.api.account.Account;
import com.tradlinx.api.account.AccountRepository;
import com.tradlinx.api.account.AccountService;
import com.tradlinx.api.account.dto.AccountLoginRequest;
import com.tradlinx.api.article.dto.*;
import com.tradlinx.api.account.dto.AccountSaveRequest;
import com.tradlinx.api.comment.Comment;
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
        // 글 작성
        String content = mockMvc.perform(post("/article")
                        .header(HttpHeaders.AUTHORIZATION, new StringStartsWith("Bearer "))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleDto)))
                .andExpect(status().isOk())
                .andExpect(authenticated()).andReturn().getResponse().getContentAsString();
        ArticleWriteResponse articleWriteResponse = objectMapper.readValue(content, ArticleWriteResponse.class);

        Article article = articleRepository.findById(articleWriteResponse.getArticleId()).orElseThrow();
        assertThat(article.getArticleTitle()).isEqualTo("articleTitle");
        assertThat(article.getArticleContents()).isEqualTo("articleContents");

        // 글 작성 3 포인트 증가
        Account account = accountRepository.findByUserId("userid").orElseThrow();
        assertThat(account.getPoints()).isEqualTo(3);
    }

    @WithUserDetails(value = "userid", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("글 수정")
    void article_update_success() throws Exception {
        // 글 작성
        Long articleId = articleService.writeArticle(articleDto);

        // 글 수정
        ArticleUpdateRequest articleUpdateRequest = new ArticleUpdateRequest();
        articleUpdateRequest.setArticleId(articleId);
        articleUpdateRequest.setArticleTitle("updateArticleTitle");
        articleUpdateRequest.setArticleContents("updateArticleContents");

        mockMvc.perform(put("/article")
                        .header(HttpHeaders.AUTHORIZATION, new StringStartsWith("Bearer "))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(authenticated())
                .andExpect(jsonPath("articleId", is(articleId.intValue())));

        Article article = articleRepository.findById(articleId).orElseThrow();
        assertThat(article.getArticleTitle()).isEqualTo("updateArticleTitle");
        assertThat(article.getArticleContents()).isEqualTo("updateArticleContents");
    }

    @WithUserDetails(value = "userid", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("글 삭제 - 포인트 감소 -3")
    void article_delete_success() throws Exception {
        // 글 작성
        Long articleId = articleService.writeArticle(articleDto);

        Account account = accountRepository.findByUserId("userid").orElseThrow();
        assertThat(account.getPoints()).isEqualTo(3);
        // 글 삭제
        mockMvc.perform(delete("/article/" + articleId)
                        .header(HttpHeaders.AUTHORIZATION, new StringStartsWith("Bearer ")))
                .andExpect(status().isOk())
                .andExpect(authenticated())
                .andExpect(jsonPath("count", is(1)));
        // 글 작성자 -3 포인트
        assertThat(articleRepository.findById(articleId)).isEmpty();
        assertThat(account.getPoints()).isEqualTo(0);
    }

    @WithUserDetails(value = "userid", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("글 조회 - commentId 응답")
    void article_get_success() throws Exception {
        // 글 작성
        Long articleId = articleService.writeArticle(articleDto);
        // 글 조회
        mockMvc.perform(get("/article/" + articleId)
                        .header(HttpHeaders.AUTHORIZATION, new StringStartsWith("Bearer ")))
                .andExpect(status().isOk())
                .andExpect(authenticated())
                .andExpect(jsonPath("articleId", is(articleId.intValue())))
                .andExpect(jsonPath("commentsId", is(new ArrayList())));
    }

    @WithUserDetails(value = "userid", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("댓글 작성 - commentId 응답, 댓글 작성자 points +2, 원글 작성자 points +1")
    void comment_write_success() throws Exception {
        // 글 작성
        Long articleId = articleService.writeArticle(articleDto);
        // 다른 사용자 로그인해서 댓글 작성
        anotherAccountAndLogin();
        CommentWriteRequest commentWriteRequest = new CommentWriteRequest();
        commentWriteRequest.setArticleId(articleId);
        commentWriteRequest.setCommentContents("commentsContents");

        mockMvc.perform(post("/comments")
                        .header(HttpHeaders.AUTHORIZATION, new StringStartsWith("Bearer "))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentWriteRequest)))
                .andExpect(status().isOk())
                .andExpect(authenticated());

        // 글작성자는 글작성 포인트 3 + 댓글 포인트 1 = 4
        Article article = articleRepository.findById(articleId).orElseThrow();
        assertThat(article.getAccount().getPoints()).isEqualTo(4);

        // 댓글 작성자는 댓글 포인트 2
        Account account = accountRepository.findByUserId("userid2").orElseThrow();
        assertThat(account.getPoints()).isEqualTo(2);
    }

    @WithUserDetails(value = "userid", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("댓글 삭제 - commentId 응답, 댓글 작성자 points -2, 원글 작성자 points -1")
    void comment_delete_success() throws Exception {
        // 글 작성
        Long articleId = articleService.writeArticle(articleDto);
        // 다른 사용자 로그인
        anotherAccountAndLogin();
        // 다른 사용자가 댓글 작성
        CommentWriteRequest commentWriteRequest = new CommentWriteRequest();
        commentWriteRequest.setArticleId(articleId);
        commentWriteRequest.setCommentContents("commentsContents");
        Long commentId = articleService.writeComment(commentWriteRequest);

        // 다른 사용자가 댓글 삭제
        mockMvc.perform(delete("/comments/" + commentId)
                        .header(HttpHeaders.AUTHORIZATION, new StringStartsWith("Bearer ")))
                .andExpect(status().isOk())
                .andExpect(authenticated())
                .andExpect(jsonPath("commentId", is(commentId.intValue())));

        // 글 작성자는 4 포인트에서 -1 포인트 = 3 포인트
        Article article = articleRepository.findById(articleId).orElseThrow();
        assertThat(article.getAccount().getPoints()).isEqualTo(3);

        // 댓글 작성자는 1 포인트에서 -1 포인트 = 0 포인트
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