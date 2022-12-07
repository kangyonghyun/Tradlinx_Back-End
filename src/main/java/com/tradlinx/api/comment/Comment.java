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

    @Id @GeneratedValue
    Long commentId;

    String commentContents;

    @ManyToOne
    @JoinColumn(name = "id")
    Article article;

    @ManyToOne
    @JoinColumn(name = "account_id")
    Account account;

    public void setArticle(Article article) {
        this.article = article;
        article.getComments().add(this);
    }

    public void setAccountAndAddPoints(Account account) {
        this.account = account;
        this.account.addCommentPoints();
        this.article.addWriterCommentPoints();
    }

    public void removeCommentAndPoints() {
        this.article.removeCommentAndPoints(this);
        this.account.minusCommentsPoint();
    }

    public void removePoints() {
        this.account.minusCommentsPoint();
    }
}
