package it.unipd.dei.eis.exceptions;

public class InvalidFileFormatException extends RuntimeException {
    public InvalidFileFormatException(String fileExtension) {
        super(fileExtension + " file extension is not supported.");
    }
}
