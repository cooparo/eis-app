package it.unipd.dei.eis.newspapers.TheGuardian;

import it.unipd.dei.eis.newspapers.TheGuardian.models.TGArticle;
import it.unipd.dei.eis.newspapers.TheGuardian.models.TGResponse;
import it.unipd.dei.eis.newspapers.TheGuardian.models.TGResponseWrapper;
import it.unipd.dei.eis.utils.Format;
import it.unipd.dei.eis.utils.HTTPClient;
import it.unipd.dei.eis.utils.Marshalling;

import java.io.IOException;
import java.util.ArrayList;

public class TGClient {

    private final static String BASE_URL = "https://content.guardianapis.com/search";
    private String apiKey;

    public TGClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public ArrayList<TGArticle> getArticleArrayList(String query, int articlesNumber) throws IOException {
        String url = BASE_URL+"?q="+query+"&page-size="+articlesNumber+"&api-key="+apiKey;

        String data = HTTPClient.get(url).getData();
        TGResponseWrapper root = Marshalling.deserialize(Format.JSON, data, TGResponseWrapper.class);

        TGResponse response = root.getResponse();
        return response.getResults();
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
