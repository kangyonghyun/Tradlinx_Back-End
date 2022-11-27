package com.tradlinx.article;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Article {

    @Id @GeneratedValue
    private Long id;
    private String articleTitle;
    private String articleContents;

}
