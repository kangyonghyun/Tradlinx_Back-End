package com.tradlinx.api.article.form;

import lombok.Data;

@Data
public class DeleteCommentDto {
    String commentId;

    public DeleteCommentDto(String commentId) {
        this.commentId = commentId;
    }
}
