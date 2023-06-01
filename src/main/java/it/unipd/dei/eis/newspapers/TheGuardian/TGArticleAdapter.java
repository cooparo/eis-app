package it.unipd.dei.eis.newspapers.TheGuardian;

import it.unipd.dei.eis.interfaces.IArticle;

import it.unipd.dei.eis.newspapers.TheGuardian.models.TGArticle;

import java.time.Instant;

public class TGArticleAdapter implements IArticle {
    private final TGArticle article;

    public TGArticleAdapter(TGArticle article) {
        this.article = article;
    }

    @Override
    public String getId() {
        return article.getId();
    }

    @Override
    public String getTitle() {
        return article.getWebTitle();
    }

    @Override
    public String getBody() {
        return article.getFields().getBodyText();
    }

    @Override
    public Instant getPublicationTime() { return article.getWebPublicationDate(); }

}
