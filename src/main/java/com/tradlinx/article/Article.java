package com.tradlinx.article;

import com.tradlinx.account.Account;
import com.tradlinx.comment.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Article {

    @Id
    @Column(name = "article_id")
    private String articleId;
    private String articleTitle;
    private String articleContents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    Account account;

    @OneToMany(mappedBy = "article")
    List<Comment> comments = new ArrayList<>();

    public void setAccount(Account account) {
        this.account = account;
        account.getArticles().add(this);
        account.addArticlePoints();
    }

    public void removeArticle(Article article) {
        this.account.getArticles().remove(article);
        this.account.minusArticlePoints();
    }

}
