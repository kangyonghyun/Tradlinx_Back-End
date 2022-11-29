package com.tradlinx.api.article.form;

import lombok.Data;

@Data
public class ArticleUpdateDto {
    private String articleId;
    private String articleTitle;
    private String articleContents;
}
