package repository;

import models.Article;
import repository.specifications.ISpecification;

import java.util.ArrayList;

public class ArticleRepository implements IRepository<Article> {

    private ArrayList<Article> storage;

    public ArticleRepository() {
        storage = ArticleStorage.getInstance().getArticleList();
    }


    @Override
    public void add(Article article) {
        storage.add(article);
    }

    @Override
    public ArrayList<Article> getAll() {
        return storage;
    }

    @Override
    public void update(Article oldArticle, Article newArticle) {
        if(storage.contains(oldArticle)) {
            int oldArticleIndex = storage.indexOf(oldArticle);
            storage.set(oldArticleIndex, newArticle);
        } else {
            //TODO: throw an Exception
        }
    }

    @Override
    public void remove(long id) {
        for(Article article : storage) {
            if(article.getId() == id) {
                storage.remove(article);
            }
        }
    }

    @Override
    public ArrayList<Article> FindBySpecification(ISpecification spec) {
        ArrayList<Article> articleArrayList = new ArrayList<>();

        for(Article article : storage) {
            if(spec.exist(article)) articleArrayList.add(article);
        }

        return articleArrayList;
    }
}
