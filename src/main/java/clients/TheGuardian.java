package clients;
import java.util.ArrayList;

public class TheGuardian implements interfaces.Newspaper {
    private String api_key;

    public TheGuardian(String api_key) {
        this.api_key = api_key;
    }

    @Override
    public Article[] getArticles() {

    }
    // public Articles[] getSavedArticles()
    public static Article[] decodeArticles(String response) {

    }

    public class Article implements interfaces.Article {
        private String id;
        private String webTitle;
        private String body = null;

        @Override
        public String getTitle() {
            return webTitle;
        }

        @Override
        public String getBody() {
            return body;
        }


    }
    public class Response {
        private String status;
        private int total;
        private ArrayList<Article> results;

        public ArrayList<Article> getResults() {
            return results;
        }
    }

}
