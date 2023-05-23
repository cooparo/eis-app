package repository.specifications;

import interfaces.ISpecification;
import models.Article;

import java.util.Objects;

public class FindByArticleId implements ISpecification<Article> {

    private String id;

    public FindByArticleId(String id) {
        this.id = id;
    }

    @Override
    public boolean exist(Article article) {
        return Objects.equals(article.getId(), this.id);
    }
}
