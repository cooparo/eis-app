package it.unipd.dei.eis.utils;

import it.unipd.dei.eis.exceptions.HTTPClientException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.UnknownHostException;

public class HTTPClient {

    private static final int TIMEOUT_TIME = 5000;

    /**
     * A wrapper for the response of the server.
     */
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

    /**
     * Sends an HTTP <b>GET</b> request to the specified URL.
     * @param url the URL to send the request to
     * @return the response from the server
     */
    public static Response get(String url) {
        return request(HTTPMethod.GET, url);
    }

    /**
     * Sends an HTTP request to the specified URL.
     * @param method the HTTP method to use
     * @param url the URL to send the request to
     * @return the response from the server
     */
    public static Response request(HTTPMethod method, String url) {

        try {
            // Create connection
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod(method.toString());
            conn.setConnectTimeout(TIMEOUT_TIME);
            conn.setReadTimeout(TIMEOUT_TIME);

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
        } catch (ProtocolException e) {
            throw new RuntimeException("Implementation error, the HTTP method " + method.toString() + " is not correct.");
        } catch (MalformedURLException e) {
            throw new HTTPClientException("The URL you've passed is malformed. Try to read the documentation.");
        } catch (UnknownHostException e) {
            throw new HTTPClientException("Probably you're not connected to the Internet. Can't connect to " + e.getMessage());
        } catch (IOException e) {
            throw new HTTPClientException(e.getMessage());
        }

    }

}
