package it.unipd.dei.eis.repository;

import it.unipd.dei.eis.interfaces.IArticle;

import java.util.ArrayList;

/**
 * A class for initializing once and only once the ArrayList of IArticle present in ArticleRepository.
 * It uses the singleton design pattern.
 */
public class ArticleStorage {
    private final ArrayList<IArticle> articleArrayList;
    private static final ArticleStorage instance = new ArticleStorage();

    private ArticleStorage() {
        articleArrayList = new ArrayList<>();
    }

    public static ArticleStorage getInstance() {
        return instance;
    }

    public ArticleStorage(ArrayList<IArticle> articleArrayList) {
        this.articleArrayList = articleArrayList;
    }

    public ArrayList<IArticle> getArticleList() {
        return articleArrayList;
    }


}
