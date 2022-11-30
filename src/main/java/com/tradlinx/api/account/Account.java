package com.tradlinx.api.account;

import com.tradlinx.api.article.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Account {

    public static final int WRITE_ARTICLE_POINTS = 3;
    public static final int WRITE_COMMENT_POINT = 2;
    public static final int WRITE_WRITER_COMMENT_POINT = 1;

    @Id @GeneratedValue
    private Long id;
    private String userId;
    private String pw;
    private String username;
    private int points;
    @OneToMany(mappedBy = "account")
    List<Article> articles = new ArrayList<>();

    public void addArticlePoints() {
        this.points += WRITE_ARTICLE_POINTS;
    }

    public void minusArticlePoints() {
        this.points -= WRITE_ARTICLE_POINTS;
    }

    public void addCommentPoints() {
        this.points += WRITE_COMMENT_POINT;
    }

    public void minusCommentsPoint() {
        this.points -= WRITE_COMMENT_POINT;
    }

    public void addWriterPoints() {
        this.points += WRITE_WRITER_COMMENT_POINT;
    }

    public void minusWriterPoints() {
        this.points -= WRITE_WRITER_COMMENT_POINT;
    }

}
