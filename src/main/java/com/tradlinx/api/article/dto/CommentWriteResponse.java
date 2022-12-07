package com.tradlinx.api.article.dto;

import lombok.Data;

@Data
public class CommentWriteResponse {
    Long commentId;

    public CommentWriteResponse(Long commentId) {
        this.commentId = commentId;
    }
}
