package it.unipd.dei.eis.interfaces;

import java.io.IOException;
import java.util.ArrayList;

public interface NewspaperController<T> {
    ArrayList<T> downloader();

    ArrayList<T> reader(String fileName) throws IOException;
}
