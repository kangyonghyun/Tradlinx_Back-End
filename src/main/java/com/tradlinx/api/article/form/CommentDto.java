package com.tradlinx.api.article.form;

import lombok.Data;

@Data
public class CommentDto {
    private String articleId;
    private String commentContents;
}
