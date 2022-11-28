package com.tradlinx.article;

import com.tradlinx.article.form.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping("/article")
    public ResponseEntity<NewArticleDto> writeArticle(@RequestBody ArticleDto articleDto) {
        String articleId = articleService.writeArticle(articleDto);
        return new ResponseEntity<>(new NewArticleDto(articleId), HttpStatus.OK);
    }

    @PutMapping("/article")
    public ResponseEntity<NewArticleDto> updateArticle(@RequestBody ArticleUpdateDto articleUpdateDto) {
        String articleId = articleService.updateArticle(articleUpdateDto);
        return new ResponseEntity<>(new NewArticleDto(articleId), HttpStatus.OK);
    }
    @DeleteMapping("/article/{articleId}")
    public ResponseEntity<DeleteArticleDto> deleteArticle(@PathVariable(name = "articleId") String articleId) {
        articleService.deleteArticle(articleId);
        return new ResponseEntity<>(new DeleteArticleDto(), HttpStatus.OK);
    }

    @GetMapping("/article/{articleId}")
    public ResponseEntity<ArticleCommentsDto> getArticle(@PathVariable(name = "articleId") String articleId) {
        ArticleCommentsDto commentsId = articleService.getCommentsOfArticle(articleId);
        return new ResponseEntity<>(commentsId, HttpStatus.OK);
    }

    @PostMapping("/comments")
    public ResponseEntity<NewCommentDto> writeComment(@RequestBody CommentDto commentDto) {
       String commentId = articleService.writeComment(commentDto);
        return new ResponseEntity<>(new NewCommentDto(commentId), HttpStatus.OK);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<NewCommentDto> deleteComment(@PathVariable("commentId") String commentId) {
        String comment = articleService.deleteComment(commentId);
        return new ResponseEntity<>(new NewCommentDto(comment), HttpStatus.OK);
    }

}
