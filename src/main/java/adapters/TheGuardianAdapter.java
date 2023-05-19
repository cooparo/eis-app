package adapters;

import models.TheGuardian.TGArticle;
import models.TheGuardian.TGRoot;

public class TheGuardianAdapter implements interfaces.IArticle {
    private final TGArticle article;

    public TheGuardianAdapter(TGArticle article) {
        this.article = article;
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
