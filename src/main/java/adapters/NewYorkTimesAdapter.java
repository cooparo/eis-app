package adapters;

import models.Article;
import models.NewYorkTimes.NYTArticle;

public class NewYorkTimesAdapter implements interfaces.IArticle{
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
}
