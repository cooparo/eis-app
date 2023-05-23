import client.TheGuardianClient;
import models.TheGuardian.TGArticle;

import java.util.ArrayList;

public class App {
    public static void main(String[] args) {

        final String API_KEY = System.getenv("API_KEY");
        TheGuardianClient client = new TheGuardianClient(API_KEY);

        ArrayList<TGArticle> articles = client.getArticleArrayList("bitcoin", 12);

        System.out.println("Size: "+articles.size());
        for (TGArticle article : articles) {
            System.out.println(article.getWebTitle());
        }

    }
}
