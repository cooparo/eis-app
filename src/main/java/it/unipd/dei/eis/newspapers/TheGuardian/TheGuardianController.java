package it.unipd.dei.eis.newspapers.TheGuardian;

import it.unipd.dei.eis.interfaces.NewspaperController;
import it.unipd.dei.eis.newspapers.TheGuardian.models.TGArticle;
import it.unipd.dei.eis.newspapers.TheGuardian.models.TGResponseWrapper;
import it.unipd.dei.eis.utils.FileFormat;
import it.unipd.dei.eis.utils.IO;
import it.unipd.dei.eis.utils.Marshalling;

import java.io.IOException;
import java.util.ArrayList;

public class TheGuardianController implements NewspaperController<TGArticle> {

    private final static String BASE_PATH = "/src/main/resources/";

    @Override
    public ArrayList<TGArticle> downloader() {
        final String API_KEY = System.getenv("API_KEY");
        TGClient client = new TGClient(API_KEY);

        ArrayList<TGArticle> articles = client.getArticleArrayList("nuclear power", 1000);
        return articles;
    }

    @Override
    public ArrayList<TGArticle> reader(String fileName) throws IOException {
        String text = IO.readFile(BASE_PATH + fileName);
        TGResponseWrapper root = Marshalling.deserialize(FileFormat.JSON, text, TGResponseWrapper.class);

        ArrayList<TGArticle> articles = root.getResponse().getResults();
        return articles;
    }
}
