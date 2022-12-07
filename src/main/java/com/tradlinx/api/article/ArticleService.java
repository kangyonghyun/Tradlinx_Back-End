package com.tradlinx.api.article;

import com.tradlinx.api.account.Account;
import com.tradlinx.api.account.AccountRepository;
import com.tradlinx.api.account.AccountService;
import com.tradlinx.api.exception.ApiException;
import com.tradlinx.api.article.dto.ArticleGetResponse;
import com.tradlinx.api.article.dto.CommentWriteRequest;
import com.tradlinx.api.comment.Comment;
import com.tradlinx.api.comment.CommentRepository;
import com.tradlinx.api.article.dto.ArticleUpdateRequest;
import com.tradlinx.api.article.dto.ArticleWriteRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

    private final ModelMapper modelMapper;
    private final ArticleRepository articleRepository;
    private final AccountRepository accountRepository;
    private final CommentRepository commentRepository;

    public Long writeArticle(ArticleWriteRequest writeRequest) {
        Account account = AccountService.getCurrentUserid()
                .flatMap(accountRepository::findByUserId)
                .orElseThrow(() -> new ApiException("Account not found"));

        Article article = modelMapper.map(writeRequest, Article.class);
        article.setAccount(account);
        return articleRepository.save(article).getArticleId();
    }

    public Long updateArticle(ArticleUpdateRequest updateRequest) {
        Account account = AccountService.getCurrentUserid()
                .flatMap(accountRepository::findByUserId)
                .orElseThrow(() -> new ApiException("Account not found"));

        Article article = articleRepository.findById(updateRequest.getArticleId())
                .orElseThrow(() -> new ApiException("Article not found"));

        // 자기가 쓴글인지 확인하는 로직 필요
        if (!account.getArticles().contains(article)) {
            throw new ApiException("글을 수정할 수 없습니다.");
        }

        modelMapper.map(updateRequest, article);
        return article.getArticleId();
    }

    public void deleteArticle(Long articleId) {
        Account account = AccountService.getCurrentUserid()
                .flatMap(accountRepository::findByUserId)
                .orElseThrow(() -> new ApiException("Account not found"));

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ApiException("Article not found"));

        // 자기가 쓴글인지 확인하는 로직 필요
        if (!account.getArticles().contains(article)) {
            throw new ApiException("글을 삭제할 수 없습니다.");
        }

        article.removeArticleAndPoints();
        articleRepository.delete(article);
    }


    public ArticleGetResponse getCommentsOfArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ApiException("Article not found"));

        ArticleGetResponse articleGetResponse = new ArticleGetResponse();
        articleGetResponse.setArticleId(article.getArticleId());
        articleGetResponse.setCommentsId(article.getComments().stream()
                .map(Comment::getCommentId).collect(Collectors.toList()));
        return articleGetResponse;
    }

    public Long writeComment(CommentWriteRequest commentWriteRequest) {
        Account account = AccountService.getCurrentUserid()
                .flatMap(accountRepository::findByUserId)
                .orElseThrow(() -> new ApiException("Account not found"));

        Article article = articleRepository.findById(commentWriteRequest.getArticleId())
                .orElseThrow(() -> new ApiException("Article not found"));

        Comment comment = modelMapper.map(commentWriteRequest, Comment.class);
        comment.setArticle(article);
        comment.setAccountAndAddPoints(account);
        return commentRepository.save(comment).getCommentId();
    }

    public Long deleteComment(Long commentId) {
        Account account = AccountService.getCurrentUserid()
                .flatMap(accountRepository::findByUserId)
                .orElseThrow(() -> new ApiException("Account not found"));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ApiException("Comment not found"));

        // 자기가 쓴 댓글인지 확인하는 로직 필요?
        if (comment.getAccount() != account) {
            throw new ApiException("댓글을 삭제할 수 없습니다.");
        }

        comment.removeCommentAndPoints();
        commentRepository.delete(comment);
        return commentId;
    }

}
