package com.tradlinx.api.comment;

import com.tradlinx.api.account.Account;
import com.tradlinx.api.article.Article;
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

    @ManyToOne
    @JoinColumn(name = "account_id")
    Account account;

    public void setArticle(Article article) {
        this.article = article;
        article.getComments().add(this);
    }

    public void setAccount(Account account) {
        this.article.getAccount().addWriterPoints();
        account.addCommentPoints();
    }

    public void removeArticle(Account account) {
        this.article.getComments().remove(article);
        this.article.getAccount().minusWriterPoints();
        account.minusCommentsPoint();
    }
}
