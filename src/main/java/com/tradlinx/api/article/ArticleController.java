package com.tradlinx.api.article;

import com.tradlinx.api.article.form.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Api(tags = {"Article API"})
public class ArticleController {

    private final ArticleService articleService;

    @ApiOperation(value = "글 작성", notes = "글 작성 API")
    @PostMapping("/article")
    public ResponseEntity<NewArticleDto> writeArticle(@RequestBody ArticleDto articleDto) {
        String articleId = articleService.writeArticle(articleDto);
        return new ResponseEntity<>(new NewArticleDto(articleId), HttpStatus.OK);
    }

    @ApiOperation(value = "글 수정", notes = "글 수정 API")
    @PutMapping("/article")
    public ResponseEntity<NewArticleDto> updateArticle(@RequestBody ArticleUpdateDto articleUpdateDto) {
        String articleId = articleService.updateArticle(articleUpdateDto);
        return new ResponseEntity<>(new NewArticleDto(articleId), HttpStatus.OK);
    }

    @ApiOperation(value = "글 삭제", notes = "글 삭제 API")
    @DeleteMapping("/article/{articleId}")
    public ResponseEntity<DeleteArticleDto> deleteArticle(@PathVariable(name = "articleId") String articleId) {
        articleService.deleteArticle(articleId);
        return new ResponseEntity<>(new DeleteArticleDto(), HttpStatus.OK);
    }

    @ApiOperation(value = "글 조회", notes = "글 조회 API")
    @GetMapping("/article/{articleId}")
    public ResponseEntity<ArticleCommentsDto> getArticle(@PathVariable(name = "articleId") String articleId) {
        ArticleCommentsDto commentsId = articleService.getCommentsOfArticle(articleId);
        return new ResponseEntity<>(commentsId, HttpStatus.OK);
    }

    @ApiOperation(value = "댓글 작성", notes = "댓글 작성 API")
    @PostMapping("/comments")
    public ResponseEntity<NewCommentDto> writeComment(@RequestBody CommentDto commentDto) {
       String commentId = articleService.writeComment(commentDto);
        return new ResponseEntity<>(new NewCommentDto(commentId), HttpStatus.OK);
    }

    @ApiOperation(value = "댓글 삭제", notes = "댓글 삭제 API")
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<NewCommentDto> deleteComment(@PathVariable("commentId") String commentId) {
        String comment = articleService.deleteComment(commentId);
        return new ResponseEntity<>(new NewCommentDto(comment), HttpStatus.OK);
    }

}
