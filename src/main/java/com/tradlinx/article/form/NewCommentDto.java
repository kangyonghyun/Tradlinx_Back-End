package com.tradlinx.article.form;

import lombok.Data;

@Data
public class NewCommentDto {
    String commentId;

    public NewCommentDto(String commentId) {
        this.commentId = commentId;
    }
}
