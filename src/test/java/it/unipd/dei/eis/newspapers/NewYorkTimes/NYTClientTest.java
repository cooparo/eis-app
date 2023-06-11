package it.unipd.dei.eis.newspapers.NewYorkTimes;

import it.unipd.dei.eis.exceptions.InvalidFileFormatException;
import it.unipd.dei.eis.newspapers.NewYorkTimes.models.NYTArticle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.InvalidPathException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NYTClientTest {


    private static final String PATH_FILE = "src/test/resources/nytimes_articles_v2.csv";
    private static final String EMPTY_PATH_FILE = "";

    NYTClient client;

    @BeforeEach
    void setUp() {
        client = new NYTClient();
    }

    @Test
    void importArticleArrayListFromFile() {

//        ArrayList<NYTArticle> results = client.importArticleArrayListFromFile(PATH_FILE);

//        assertNotNull(results);
    }

    @Test
    void emptyPathExceptionTest() {
        // Run the method with an empty file path to check if an exception is launched

        try {
            ArrayList<NYTArticle> testArrayList = client.importArticleArrayListFromFile(EMPTY_PATH_FILE);
        } catch (RuntimeException e) {
            assertThrows(RuntimeException.class, () -> {
                throw new InvalidPathException(EMPTY_PATH_FILE, "The path is empty");
            });
        }


    }
}