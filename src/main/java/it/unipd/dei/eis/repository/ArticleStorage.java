package it.unipd.dei.eis.repository;

import java.util.ArrayList;

public class ArticleStorage {
    private final ArrayList<Article> articleArrayList;
    private static final ArticleStorage instance = new ArticleStorage();

    private ArticleStorage() {
        articleArrayList = new ArrayList<>();
    }

    public static ArticleStorage getInstance() {
        return instance;
    }

    public ArticleStorage(ArrayList<Article> articleArrayList) {
        this.articleArrayList = articleArrayList;
    }

    public ArrayList<Article> getArticleList() {
        return articleArrayList;
    }


}
