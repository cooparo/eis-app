package it.unipd.dei.eis.exceptions;

public class MalformedPackageException extends RuntimeException {
    public MalformedPackageException(String newspaper, String reason) {
        super(newspaper + " package must follow the structure described in the documentation. " +
            "The reason of the exception is the following: " + reason);
    }
}