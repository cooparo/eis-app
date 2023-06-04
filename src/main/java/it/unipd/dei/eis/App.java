package it.unipd.dei.eis;

import it.unipd.dei.eis.interfaces.IArticle;
import it.unipd.dei.eis.newspapers.TheGuardian.TGArticleAdapter;
import it.unipd.dei.eis.newspapers.TheGuardian.TGClient;
import it.unipd.dei.eis.newspapers.TheGuardian.models.TGArticle;
import it.unipd.dei.eis.newspapers.TheGuardian.models.TGResponseWrapper;
import it.unipd.dei.eis.ranker.Ranker;
import it.unipd.dei.eis.repository.Article;
import it.unipd.dei.eis.repository.ArticleRepository;
import it.unipd.dei.eis.utils.Format;
import it.unipd.dei.eis.utils.IO;
import it.unipd.dei.eis.utils.Marshalling;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Function;

public class App {
    private static final ArticleRepository repository = new ArticleRepository();

    public static void main(String[] args) throws IOException {
        download();

        repository.loadFromDisk();
        System.out.println(repository.getAll().get(0).getTitle());
        System.out.println(repository.getAll().get(0).getBody());

//        Ranker ranker = new Ranker();
//        ranker.rank(repository.getAll());
//        ranker .saveOnTxt("ranked.txt");
    }
    public static void download() throws IOException {
        theGuardianDownloader();
    }

    public static void theGuardianDownloader() throws IOException {
        final String API_KEY = System.getenv("API_KEY");
        TGClient client = new TGClient(API_KEY);

        ArrayList<TGArticle> articles = client.getArticleArrayList("bitcoin", 100);

        addDataToRepository(articles, TGArticleAdapter::new);

    }
    public static void theGuardianReader() throws IOException {
        String text = IO.readFile("./src/main/resources/the_guardian_response_1.json");
        TGResponseWrapper root = Marshalling.deserialize(Format.JSON, text, TGResponseWrapper.class);

        ArrayList<TGArticle> articles = root.getResponse().getResults();
        addDataToRepository(articles, TGArticleAdapter::new);
    }

    private static <NewspaperArticle> void addDataToRepository(ArrayList<NewspaperArticle> articles, Function<NewspaperArticle, IArticle> converter) throws IOException {
        for (NewspaperArticle article : articles) {
            repository.add(new Article(converter.apply(article)));
        }
        repository.saveToDisk();
    }
}