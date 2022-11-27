package com.tradlinx.article;

import com.tradlinx.account.Account;
import com.tradlinx.account.AccountRepository;
import com.tradlinx.account.AccountService;
import com.tradlinx.article.form.ArticleCommentsDto;
import com.tradlinx.article.form.ArticleUpdateDto;
import com.tradlinx.article.form.ArticleDto;
import com.tradlinx.article.form.CommentDto;
import com.tradlinx.comment.Comment;
import com.tradlinx.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

    private final ModelMapper modelMapper;
    private final ArticleRepository articleRepository;
    private final AccountRepository accountRepository;
    private final CommentRepository commentRepository;

    public String writeArticle(ArticleDto articleDto) {
        Account account = AccountService.getCurrentUserid()
                .flatMap(accountRepository::findOneWithByUserid)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        Article article = modelMapper.map(articleDto, Article.class);
        article.setArticleId("articleId");
        article.setAccount(account);
        return articleRepository.save(article).getArticleId();
    }

    public String updateArticle(ArticleUpdateDto articleUpdateDto) {
        Article article = articleRepository.findById(articleUpdateDto.getArticleId())
                .orElseThrow(() -> new IllegalArgumentException("Article not found"));
        modelMapper.map(articleUpdateDto, article);
        return article.getArticleId();
    }

    public void deleteArticle(String articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found"));
        article.removeArticle(article);
        articleRepository.delete(article);
    }


    public ArticleCommentsDto getCommentsOfArticle(String articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found"));

        List<String> allComments = article.getComments().stream()
                .map(Comment::getCommentId).collect(Collectors.toList());

        ArticleCommentsDto articleCommentsDto = new ArticleCommentsDto();
        articleCommentsDto.setArticleId(articleId);
        articleCommentsDto.setCommentsId(allComments);
        return articleCommentsDto;
    }

    public String writeComment(CommentDto commentDto) {
        Article article = articleRepository.findById(commentDto.getArticleId())
                .orElseThrow(() -> new IllegalArgumentException("Article not found"));

        Account account = AccountService.getCurrentUserid()
                .flatMap(accountRepository::findOneWithByUserid)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        Comment comment = modelMapper.map(commentDto, Comment.class);
        comment.setCommentId("commentId");
        comment.setArticle(article);
        comment.setAccount(account);
        return commentRepository.save(comment).getCommentId();
    }

    public String deleteComment(String commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        Account account = AccountService.getCurrentUserid()
                .flatMap(accountRepository::findOneWithByUserid)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        comment.removeArticle(account);
        commentRepository.delete(comment);
        return comment.getCommentId();
    }
}
