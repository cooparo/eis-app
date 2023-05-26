package it.unipd.dei.eis.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPClient {
    public static class Response {
        private final int status;
        private final String data;

        Response(int status, String data) {
            this.status = status;
            this.data = data;
        }
        public int getStatus() {
            return status;
        }
        public String getData() {
            return data;
        }
    }

    public static Response get(String url) throws IOException {
        return request("GET", url);
    }
    public static Response request(String method, String url) throws IOException {
        // Create connection
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod(method);
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);

        // Turning the Input Stream into a String
        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        // Freeing resources
        in.close();
        conn.disconnect();

        return new Response(conn.getResponseCode(), content.toString());
    }
}
