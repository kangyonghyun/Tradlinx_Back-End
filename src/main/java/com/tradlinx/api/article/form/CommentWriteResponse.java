package com.tradlinx.api.article.form;

import lombok.Data;

@Data
public class CommentWriteResponse {
    Long commentId;

    public CommentWriteResponse(Long commentId) {
        this.commentId = commentId;
    }
}
