package it.unipd.dei.eis.core;

import it.unipd.dei.eis.newspapers.TheGuardian.TGArticleAdapter;
import it.unipd.dei.eis.newspapers.TheGuardian.TGClient;
import it.unipd.dei.eis.newspapers.TheGuardian.models.TGArticle;
import it.unipd.dei.eis.newspapers.TheGuardian.models.TGResponseWrapper;
import it.unipd.dei.eis.repository.ArticleRepository;
import it.unipd.dei.eis.utils.FileFormat;
import it.unipd.dei.eis.utils.IO;
import it.unipd.dei.eis.utils.Marshalling;

import java.io.IOException;
import java.util.ArrayList;

public class Downloader {
    private final ArticleRepository repository;

    public Downloader(ArticleRepository repository) {
        this.repository = repository;
    }

    public void theGuardianDownloader() {
        final String API_KEY = System.getenv("API_KEY");
        TGClient client = new TGClient(API_KEY);

        ArrayList<TGArticle> articles = client.getArticleArrayList("nuclear power", 1000);

        repository.add(articles, TGArticleAdapter::new);

    }

    public void theGuardianReader() throws IOException {
        String text = IO.readFile("./src/main/resources/the_guardian_response_1.json");
        TGResponseWrapper root = Marshalling.deserialize(FileFormat.JSON, text, TGResponseWrapper.class);

        ArrayList<TGArticle> articles = root.getResponse().getResults();
        repository.add(articles, TGArticleAdapter::new);
    }


}
