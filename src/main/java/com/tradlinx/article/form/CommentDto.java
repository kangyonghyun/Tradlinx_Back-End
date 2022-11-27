package com.tradlinx.article.form;

import lombok.Data;

@Data
public class CommentDto {
    private String articleId;
    private String commentContents;
}
