package com.tradlinx.article;

import com.tradlinx.article.form.ArticleCommentsDto;
import com.tradlinx.article.form.ArticleDto;
import com.tradlinx.article.form.ArticleUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping("/article")
    public ResponseEntity<String> write(@RequestBody ArticleDto articleDto) {
        String articleId = articleService.writeArticle(articleDto);
        return new ResponseEntity<>(articleId, HttpStatus.OK);
    }

    @PutMapping("/article")
    public ResponseEntity<String> update(@RequestBody ArticleUpdateDto articleUpdateDto) {
        String articleId = articleService.updateArticle(articleUpdateDto);
        return new ResponseEntity<>(articleId, HttpStatus.OK);
    }
    @DeleteMapping("/article/{articleId}")
    public ResponseEntity<String> delete(@PathVariable(name = "articleId") String articleId) {
        articleService.deleteArticle(articleId);
        return new ResponseEntity("1", HttpStatus.OK);
    }

    @GetMapping("/article/{articleId}")
    public ResponseEntity<ArticleCommentsDto> article(@PathVariable(name = "articleId") String articleId) {
        ArticleCommentsDto commentsId = articleService.getCommentsOfArticle(articleId);
        return new ResponseEntity(commentsId, HttpStatus.OK);
    }

}
