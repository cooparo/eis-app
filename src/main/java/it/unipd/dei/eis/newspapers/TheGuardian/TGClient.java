package it.unipd.dei.eis.newspapers.TheGuardian;

import it.unipd.dei.eis.newspapers.TheGuardian.models.TGArticle;
import it.unipd.dei.eis.newspapers.TheGuardian.models.TGResponse;
import it.unipd.dei.eis.newspapers.TheGuardian.models.TGResponseWrapper;
import it.unipd.dei.eis.utils.Format;
import it.unipd.dei.eis.utils.HTTPClient;
import it.unipd.dei.eis.utils.Marshalling;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class TGClient {

    private final static String BASE_URL = "https://content.guardianapis.com/search";
    private final static String ENCODING_UTF8 = "UTF-8";
    private final static int MAX_PAGE_SIZE = 200;
    private String apiKey;

    public TGClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public ArrayList<TGArticle> getArticleArrayList(String query, int articlesNumber) {
        if (articlesNumber < 0) throw new IllegalArgumentException("The number of articles must be a positive integer");

        // Encode the query, e.g. "nuclear power" -> "nuclear%20power"
        final String formattedQuery;
        try {
            formattedQuery = URLEncoder.encode(query, ENCODING_UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        // If the number of articles is greater than 200, we need to make multiple requests
        int pagesNumber = articlesNumber > 200 ? (int) Math.ceil(articlesNumber / 200.0) : 1;
//        System.out.println("Number of pages: " + pagesNumber);

        ArrayList<TGArticle> results = new ArrayList<>();

        for (int i = 1; i <= pagesNumber; i++) {

            // The last page may have a different page size
            int pageSize;
            if (i == pagesNumber && articlesNumber % 200 != 0) { // Last page
                pageSize = articlesNumber - MAX_PAGE_SIZE * (pagesNumber - 1);
            } else pageSize = Math.min(articlesNumber, MAX_PAGE_SIZE);

            TGResponseWrapper root;
            String url = BASE_URL + "?q=" + formattedQuery + "&page=" + i + "&show-fields=body-text&page-size=" + pageSize + "&api-key=" + apiKey;

            String data = HTTPClient.get(url).getData();
            root = Marshalling.deserialize(Format.JSON, data, TGResponseWrapper.class);

            TGResponse response = root.getResponse();

            results.addAll(response.getResults());
            System.out.println("Number of articles: " + results.size());


            System.out.println("Page " + i + " of " + pagesNumber + " downloaded");
            System.out.println("Page size: " + pageSize);
        }


        return results;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
