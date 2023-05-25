import adapters.TheGuardianAdapter;
import clients.remote.TheGuardianClient;
import interfaces.IArticle;
import models.Article;
import models.TheGuardian.TGArticle;
import repository.ArticleRepository;

import java.util.ArrayList;
import java.util.function.Function;

public class App {
    private static final ArticleRepository repository = new ArticleRepository();

    public static void main(String[] args) {
        download();
        System.out.println(repository.getAll().get(0).getTitle());
    }
    public static void download() {
        TheGuardianDownloader();
    }
    public static void rank() {

    }

    public static void TheGuardianDownloader() {
        final String API_KEY = "API_KEY";
        TheGuardianClient client = new TheGuardianClient(API_KEY);

        ArrayList<TGArticle> articles = client.getArticleArrayList("bitcoin", 12);

        addDataToRepository(articles, (article) -> new TheGuardianAdapter(article));
    }

    private static <NewspaperArticle> void addDataToRepository(ArrayList<NewspaperArticle> articles, Function<NewspaperArticle, IArticle> converter) {
        for (NewspaperArticle article : articles) {
            repository.add(new Article(converter.apply(article)));
        }
    }
}