package interfaces;

import entities.Article;
import java.util.List;

public interface NewsDowloader {

    public List<Article> getArticles(String keyword);
    public List<Article> getArticles();

}
