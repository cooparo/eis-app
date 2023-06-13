package it.unipd.dei.eis.newspapers.TheGuardian;

import it.unipd.dei.eis.exceptions.InvalidFileFormatException;
import it.unipd.dei.eis.newspapers.TheGuardian.models.TGArticle;
import it.unipd.dei.eis.newspapers.TheGuardian.models.TGResponse;
import it.unipd.dei.eis.newspapers.TheGuardian.models.TGResponseWrapper;
import it.unipd.dei.eis.utils.FileFormat;
import it.unipd.dei.eis.utils.HTTPClient;
import it.unipd.dei.eis.utils.IO;
import it.unipd.dei.eis.utils.Marshalling;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TGClient {

    private final static String BASE_URL = "https://content.guardianapis.com/search";
    private final static String ENCODING_UTF8 = "UTF-8";
    private final static int MAX_PAGE_SIZE = 200;
    private final static int MAX_ARTICLES_NUMBER = 2000;
    private String apiKey;

    public TGClient(String apiKey) {
        this.apiKey = apiKey;
    }

    /***
     * Fetches articles from The Guardian API. Use multiple threads to speed up the process.
     * @param query The query to be searched
     * @param articlesNumber The number of articles to be fetched
     * @return An ArrayList of TGArticle objects
     */
    public ArrayList<TGArticle> getArticleArrayList(String query, int articlesNumber) {
        if (articlesNumber < 0) throw new IllegalArgumentException("The number of articles must be a positive integer");
        if (articlesNumber > MAX_ARTICLES_NUMBER)
            throw new IllegalArgumentException("Too many articles requested, max " + MAX_ARTICLES_NUMBER + " articles allowed");

        // Encode the query, e.g. "nuclear power" -> "nuclear%20power"
        final String formattedQuery;
        try {
            formattedQuery = URLEncoder.encode(query, ENCODING_UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        // If the number of articles is greater than 200, we need to make multiple requests
        int pagesNumber = articlesNumber > 200 ? (int) Math.ceil(articlesNumber / 200.0) : 1;

        ArrayList<TGArticle> results = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (int i = 1; i <= pagesNumber; i++) {
            int currentPageSize = Math.min(articlesNumber - (i - 1) * MAX_PAGE_SIZE, MAX_PAGE_SIZE);
            final int currentPage = i;

            executorService.execute(() -> {
                ArrayList<TGArticle> pageResults = fetchArticles(formattedQuery, currentPage, currentPageSize);
                synchronized (results) { // Synchronize the access to the results ArrayList
                    results.addAll(pageResults);
                }
            });
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        return results;
    }

    /***
     * Imports an ArrayList of TGArticle from a file.
     * @param path The path of the file to import.
     * @return The imported articles.
     */
    public ArrayList<TGArticle> importArticleArrayListFromFile(String path) {
        try {
            String data = IO.readFile(path);
            final FileFormat fileFormat = FileFormat.valueOf(IO.getFileExtension(path).toUpperCase());

            TGResponseWrapper root = Marshalling.deserialize(fileFormat, data, TGResponseWrapper.class);
            return root.getResponse().getResults();

        } catch (IllegalArgumentException e) {
            throw new InvalidFileFormatException(IO.getFileExtension(path));
        } catch (IOException e) {
            throw new RuntimeException("Could not load from disk your file.", e);
        }
    }

    /***
     * Fetches articles from The Guardian API.
     * @param query The query to search for.
     * @param page The page number.
     * @param pageSize The number of articles per page.
     * @return An ArrayList of TGArticle objects.
     */
    private ArrayList<TGArticle> fetchArticles(String query, int page, int pageSize) {
        final String showFields = "body-text";
        String url = String.format("%s?q=%s&page=%d&page-size=%d&show-fields=%s&api-key=%s",
                BASE_URL, query, page, pageSize, showFields, apiKey);

        String data = HTTPClient.get(url).getData();
        TGResponseWrapper root = Marshalling.deserialize(FileFormat.JSON, data, TGResponseWrapper.class);
        TGResponse response = root.getResponse();

        return response.getResults();
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
