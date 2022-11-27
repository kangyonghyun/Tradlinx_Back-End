package com.tradlinx.article;

import com.tradlinx.account.Account;
import com.tradlinx.account.AccountRepository;
import com.tradlinx.account.AccountService;
import com.tradlinx.account.form.ArticleUpdateDto;
import com.tradlinx.article.form.ArticleDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

    private final ModelMapper modelMapper;
    private final ArticleRepository articleRepository;
    private final AccountRepository accountRepository;

    public String writeArticle(ArticleDto articleDto) {
        Account account = AccountService.getCurrentUserid()
                .flatMap(accountRepository::findOneWithByUserid)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        account.addPoints();

        Article article = modelMapper.map(articleDto, Article.class);
        article.setArticleId("articleId");
        return articleRepository.save(article).getArticleId();
    }

    public String updateArticle(ArticleUpdateDto articleUpdateDto) {
        Article article = articleRepository.findById(articleUpdateDto.getArticleId())
                .orElseThrow(() -> new IllegalArgumentException("Article not found"));
        modelMapper.map(articleUpdateDto, article);
        articleRepository.save(article);
        return article.getArticleId();
    }

    public void deleteArticle(String articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found"));
        articleRepository.delete(article);

        Account account = AccountService.getCurrentUserid()
                .flatMap(accountRepository::findOneWithByUserid)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        account.minusPoints();
    }

}
