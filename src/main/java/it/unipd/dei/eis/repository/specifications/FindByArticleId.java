package it.unipd.dei.eis.repository.specifications;

import it.unipd.dei.eis.interfaces.ISpecification;
import it.unipd.dei.eis.repository.Article;

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
