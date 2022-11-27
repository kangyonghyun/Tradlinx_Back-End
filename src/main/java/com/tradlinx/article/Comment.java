package com.tradlinx.article;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Comment {

    @Id @Column(name = "comment_id")
    String commentId;

    String commentContents;

    @ManyToOne
    @JoinColumn(name = "article_id")
    Article article;

    public void setArticle(Article article) {
        this.article = article;
        article.getComments().add(this);
    }
}
