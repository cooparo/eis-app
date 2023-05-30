package it.unipd.dei.eis;

import it.unipd.dei.eis.adapters.TheGuardianAdapter;
import it.unipd.dei.eis.clients.remote.TheGuardianClient;
import it.unipd.dei.eis.interfaces.IArticle;
import it.unipd.dei.eis.models.Article;
import it.unipd.dei.eis.models.TheGuardian.TGArticle;
import it.unipd.dei.eis.models.TheGuardian.TGResponseWrapper;
import it.unipd.dei.eis.repository.ArticleRepository;
import it.unipd.dei.eis.utils.Format;
import it.unipd.dei.eis.utils.IO;
import it.unipd.dei.eis.utils.Marshalling;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;

public class App {
    private static final ArticleRepository repository = new ArticleRepository();

    public static void main(String[] args) throws IOException {
        download();
        System.out.println(repository.getAll().get(0).getTitle());
    }
    public static void download() throws IOException {
        TheGuardianReader();
    }

    public static void TheGuardianDownloader() throws IOException {
        final String API_KEY = "API_KEY";
        TheGuardianClient client = new TheGuardianClient(API_KEY);

        ArrayList<TGArticle> articles = client.getArticleArrayList("bitcoin", 12);

        addDataToRepository(articles, (article) -> new TheGuardianAdapter(article));
    }
    public static void TheGuardianReader() throws IOException {
        String text = IO.readFile("./src/main/resources/the_guardian_response_1.json");
        TGResponseWrapper root = Marshalling.deserialize(Format.JSON, text, TGResponseWrapper.class);

        ArrayList<TGArticle> articles = root.getResponse().getResults();
        addDataToRepository(articles, (article) -> new TheGuardianAdapter(article));
    }

    private static <NewspaperArticle> void addDataToRepository(ArrayList<NewspaperArticle> articles, Function<NewspaperArticle, IArticle> converter) throws IOException {
        for (NewspaperArticle article : articles) {
            repository.add(new Article(converter.apply(article)));
        }
        repository.saveToDisk();
    }
}