package com.tradlinx.api.article.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ArticleUpdateRequest {

    private Long articleId;

    @NotBlank
    private String articleTitle;

    private String articleContents;

}
