package it.unipd.dei.eis.adapters;

import it.unipd.dei.eis.interfaces.IArticle;
import it.unipd.dei.eis.models.TheGuardian.TGArticle;

public class TheGuardianAdapter implements IArticle {
    private final TGArticle article;

    public TheGuardianAdapter(TGArticle article) {
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
}
