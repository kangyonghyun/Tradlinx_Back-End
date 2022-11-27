package com.tradlinx.article;

import com.tradlinx.account.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
    List<Comment> comments;

}
