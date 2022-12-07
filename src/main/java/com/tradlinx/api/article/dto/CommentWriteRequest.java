package com.tradlinx.api.article.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CommentWriteRequest {

    private Long articleId;

    @NotBlank
    private String commentContents;

}
