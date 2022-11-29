package com.tradlinx.api.article.form;

import lombok.Data;

@Data
public class NewArticleDto {
    private String articleId;

    public NewArticleDto(String articleId) {
        this.articleId = articleId;
    }
}
