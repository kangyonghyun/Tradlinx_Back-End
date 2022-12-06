package com.tradlinx.api.article.form;

import lombok.Data;

@Data
public class ArticleUpdateRequest {
    private Long articleId;
    private String articleTitle;
    private String articleContents;
}
