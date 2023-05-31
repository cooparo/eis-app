package it.unipd.dei.eis.adapters;

import it.unipd.dei.eis.models.NewYorkTimes.NYTArticle;

import java.time.Instant;

public class NewYorkTimesAdapter implements it.unipd.dei.eis.interfaces.IArticle{
    private final NYTArticle article;

    public NewYorkTimesAdapter(NYTArticle article) {
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
