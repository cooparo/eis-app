package adapters;

import entities.Article;
import interfaces.NewsDowloader;
import java.util.List;

public class TheGuardianAdapter implements NewsDowloader {
    private final static String BASE_URL = "https://content.guardianapis.com/search";
    private final String API_KEY;

    TheGuardianAdapter(String API_KEY) {
        this.API_KEY = API_KEY;
    }

    private String getUrl(String keyword) {
        return BASE_URL + "?q=" + keyword + "&api-key=" + API_KEY;
    }

    @Override
    public List<Article> getArticles(String keyword) {
        return null;
    }

    @Override
    public List<Article> getArticles() {
        return getArticles("");
    }
}
