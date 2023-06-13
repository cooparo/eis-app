package it.unipd.dei.eis.exceptions;

/**
 * Exception thrown by utils.HTTPClient class.
 */
public class HTTPClientException extends RuntimeException {
    public HTTPClientException(String message) {
        super(message);
    }
}
