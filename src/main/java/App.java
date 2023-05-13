import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws IOException {

        String baseUrl = "https://content.guardianapis.com/search";

        String API_KEY = "";

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a topic: ");
        String topic = "bitcoin"; //scanner.nextLine();
        System.out.println("You entered: " + topic);

        String query = "q=" + topic;
        String format = "format=json";

        URL url = null;
        try {
            url = new URL(baseUrl + "?" + query + "&" + format + "&api-key=" + API_KEY);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
//        System.out.println(url);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);

        int status = conn.getResponseCode();
        System.out.println("Status: " + status);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        conn.disconnect();

        // Get an article's list
        // System.out.println(content);

        ObjectMapper mapper = new ObjectMapper();
        Root root = mapper.readValue(content.toString(), Root.class);
        Response response = root.getResponse();

        System.out.println("Response status: " + response.getStatus());

        // Get article's body
        Article article = response.getResults().get(0);
        System.out.println("Response[0] web title: " + article.getWebTitle());
        System.out.println("Response[0] body: " + article.getBody());


    }
}
