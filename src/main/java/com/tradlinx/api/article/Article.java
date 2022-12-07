package com.tradlinx.api.article;

import com.tradlinx.api.account.Account;
import com.tradlinx.api.comment.Comment;
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

    @Id @GeneratedValue
    private Long articleId;

    private String articleTitle;

    private String articleContents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    Account account;

    @OneToMany(mappedBy = "article", orphanRemoval = true)
    List<Comment> comments = new ArrayList<>();

    public void setAccount(Account account) {
        this.account = account;
        account.getArticles().add(this);
        account.addArticlePoints();
    }

    public void removeArticleAndPoints() {
        this.account.removeArticle(this);
        this.account.minusArticlePoints();
        for (Comment comment : comments) {
            this.account.minusWriterCommentPoints();
            comment.removePoints();
        }
    }

    public void removeCommentAndPoints(Comment comment) {
        this.comments.remove(comment);
        this.account.minusWriterCommentPoints();
    }
}
