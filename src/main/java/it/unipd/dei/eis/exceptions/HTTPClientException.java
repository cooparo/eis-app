package it.unipd.dei.eis.exceptions;

public class HTTPClientException extends RuntimeException {
    public HTTPClientException(String message) {
        super(message);
    }
}
