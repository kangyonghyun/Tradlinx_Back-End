package com.tradlinx.api.article;

import com.tradlinx.api.article.dto.*;
import com.tradlinx.api.article.validator.ArticleWriteValidator;
import com.tradlinx.api.exception.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Api(tags = {"Article API"})
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleWriteValidator articleWriteValidator;

    @InitBinder("articleWriteRequest")
    public void initBind(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(articleWriteValidator);
    }

    @ApiOperation(value = "글 작성", notes = "글 작성 API")
    @PostMapping("/article")
    public ArticleWriteResponse writeArticle(@RequestBody @Valid ArticleWriteRequest writeRequest) {
        Long articleId = articleService.writeArticle(writeRequest);
        return new ArticleWriteResponse(articleId);
    }

    @ApiOperation(value = "글 수정", notes = "글 수정 API")
    @PutMapping("/article")
    public ArticleUpdateResponse updateArticle(@RequestBody @Valid ArticleUpdateRequest updateRequest, Errors errors) {
        if (errors.hasErrors()) {
            throw new ApiException("제목을 입력해주세요.");
        }
        Long articleId = articleService.updateArticle(updateRequest);
        return new ArticleUpdateResponse(articleId);
    }

    @ApiOperation(value = "글 삭제", notes = "글 삭제 API")
    @DeleteMapping("/article/{articleId}")
    public ArticleDeleteResponse deleteArticle(@PathVariable(name = "articleId") Long articleId) {
        articleService.deleteArticle(articleId);
        return new ArticleDeleteResponse(1);
    }

    @ApiOperation(value = "글 조회", notes = "글 조회 API")
    @GetMapping("/article/{articleId}")
    public ArticleGetResponse getArticle(@PathVariable(name = "articleId") Long articleId) {
        return articleService.getCommentsOfArticle(articleId);
    }

    @ApiOperation(value = "댓글 작성", notes = "댓글 작성 API")
    @PostMapping("/comments")
    public CommentWriteResponse writeComment(@RequestBody @Valid CommentWriteRequest commentWriteRequest,
                                             Errors errors) {
        if (errors.hasErrors()) {
            throw new ApiException("내용을 입력해주세요.");
        }
        Long commentId = articleService.writeComment(commentWriteRequest);
        return new CommentWriteResponse(commentId);
    }

    @ApiOperation(value = "댓글 삭제", notes = "댓글 삭제 API")
    @DeleteMapping("/comments/{commentId}")
    public CommentWriteResponse deleteComment(@PathVariable("commentId") Long commentId) {
        Long comment = articleService.deleteComment(commentId);
        return new CommentWriteResponse(comment);
    }

}
