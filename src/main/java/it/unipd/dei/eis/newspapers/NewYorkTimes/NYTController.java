package it.unipd.dei.eis.newspapers.NewYorkTimes;

import it.unipd.dei.eis.interfaces.NewspaperController;
import it.unipd.dei.eis.newspapers.NewYorkTimes.models.NYTArticle;

import java.io.IOException;
import java.util.ArrayList;

public class NYTController implements NewspaperController<NYTArticle> {
    @Override
    public ArrayList<NYTArticle> downloader() {
        return null;
    }

    @Override
    public ArrayList<NYTArticle> reader(String fileName) throws IOException {
        return null;
    }
}
