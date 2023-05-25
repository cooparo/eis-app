package it.unipd.dei.eis.clients.remote;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.unipd.dei.eis.models.TheGuardian.TGArticle;
import it.unipd.dei.eis.models.TheGuardian.TGResponse;
import it.unipd.dei.eis.models.TheGuardian.TGResponseWrapper;
import it.unipd.dei.eis.utils.Format;
import it.unipd.dei.eis.utils.Marshalling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class TheGuardianClient {

    private final static String BASE_URL = "https://content.guardianapis.com/search";
    private String apiKey;

    public TheGuardianClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public ArrayList<TGArticle> getArticleArrayList(String query, int articlesNumber) {

        URL url;
        try {
            url = new URL(BASE_URL+"?q="+query+"&page-size="+articlesNumber+"&api-key="+apiKey); //FIXME: crea meglio l'URL
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        HttpURLConnection conn;
                StringBuffer content;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String inputLine;
            content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        conn.disconnect();

        TGResponseWrapper root;
        try {
            root = Marshalling.deserialize(Format.JSON, content.toString(), TGResponseWrapper.class);
        } catch (JsonProcessingException e) { throw new RuntimeException(e); }

        TGResponse response = root.getResponse();
        ArrayList<TGArticle> TGarticleArrayList = response.getResults();

        return TGarticleArrayList;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
