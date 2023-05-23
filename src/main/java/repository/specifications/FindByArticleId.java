package repository.specifications;

import models.Article;

public class FindByArticleId implements ISpecification<Article> {

    private long id;

    public FindByArticleId(long id) {
        this.id = id;
    }

    @Override
    public boolean exist(Article article) {
        return article.getId()==this.id;
    }
}
