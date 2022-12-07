package com.tradlinx.api.article.dto;

import lombok.Data;

import java.util.List;

@Data
public class ArticleGetResponse {
    private Long articleId;
    private List<Long> commentsId;
}
