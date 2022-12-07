package com.tradlinx.api.article.form;

import lombok.Data;

@Data
public class CommentWriteRequest {
    private Long articleId;
    private String commentContents;
}
