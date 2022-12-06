package com.tradlinx.api.article.form;

import lombok.Data;

@Data
public class ArticleWriteResponse {
    private Long articleId;

    public ArticleWriteResponse(Long articleId) {
        this.articleId = articleId;
    }
}
