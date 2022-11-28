package com.tradlinx.article.form;

import lombok.Data;

@Data
public class NewArticleDto {
    private String articleId;

    public NewArticleDto(String articleId) {
        this.articleId = articleId;
    }
}
