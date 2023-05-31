package it.unipd.dei.eis.clients.remote;

import it.unipd.dei.eis.models.TheGuardian.TGArticle;
import it.unipd.dei.eis.models.TheGuardian.TGResponse;
import it.unipd.dei.eis.models.TheGuardian.TGResponseWrapper;
import it.unipd.dei.eis.utils.Format;
import it.unipd.dei.eis.utils.HTTPClient;
import it.unipd.dei.eis.utils.Marshalling;

import java.io.IOException;
import java.util.ArrayList;

public class TheGuardianClient {

    private final static String BASE_URL = "https://content.guardianapis.com/search";
    private String apiKey;

    public TheGuardianClient(String apiKey) {
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
