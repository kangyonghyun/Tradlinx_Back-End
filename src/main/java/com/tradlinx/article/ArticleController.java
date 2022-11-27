package com.tradlinx.article;

import com.tradlinx.account.form.ArticleUpdateDto;
import com.tradlinx.article.form.ArticleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

}
