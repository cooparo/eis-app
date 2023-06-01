package it.unipd.dei.eis.newspapers.NewYorkTimes;

import it.unipd.dei.eis.newspapers.NewYorkTimes.models.NYTArticle;

import java.time.Instant;

public class NYTArticleAdapter implements it.unipd.dei.eis.interfaces.IArticle{
    private final NYTArticle article;

    public NYTArticleAdapter(NYTArticle article) {
        this.article = article;
    }

    @Override
    public String getId() {
        return article.getIdentifier();
    }

    @Override
    public String getTitle() {
        return article.getTitle();
    }

    @Override
    public String getBody() {
        return article.getBody();
    }

    @Override
    public Instant getPublicationTime() { return article.getDate(); }

}
