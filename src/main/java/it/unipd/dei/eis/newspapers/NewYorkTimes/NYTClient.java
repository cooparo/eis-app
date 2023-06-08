package it.unipd.dei.eis.newspapers.NewYorkTimes;

import it.unipd.dei.eis.exceptions.InvalidFileFormatException;
import it.unipd.dei.eis.newspapers.NewYorkTimes.models.NYTArticle;
import it.unipd.dei.eis.utils.FileFormat;
import it.unipd.dei.eis.utils.IO;
import it.unipd.dei.eis.utils.Marshalling;

import java.io.IOException;
import java.util.ArrayList;

public class NYTClient {
    public NYTClient() {}

    public ArrayList<NYTArticle> importArticleArrayListFromFile(String path) {
        try {
            String data = IO.readFile(path);
            final FileFormat fileFormat = FileFormat.valueOf(IO.getFileExtension(path).toUpperCase());

            return Marshalling.deserialize(fileFormat, data, new Marshalling.TypeReference<ArrayList<NYTArticle>>(){});

        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("No enum constant"))
                throw new InvalidFileFormatException(IO.getFileExtension(path));
            else throw e;
        } catch (IOException e) {
            throw new RuntimeException("Could not load from disk your file.", e);
        }
    }
}
