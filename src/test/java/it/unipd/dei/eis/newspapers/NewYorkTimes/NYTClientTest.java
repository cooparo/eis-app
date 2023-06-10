package it.unipd.dei.eis.newspapers.NewYorkTimes;

import it.unipd.dei.eis.exceptions.InvalidFileFormatException;
import it.unipd.dei.eis.newspapers.NewYorkTimes.models.NYTArticle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;

class NYTClientTest {
    //TODO

    // Set up a NYTClient instance
    private NYTClient newYorkTimesClient;
    private String targetPath;
    private final String file_path = "src/test/resources/nytimes_test_articles.csv";

    @BeforeEach
    void setUp() {
        newYorkTimesClient = new NYTClient();
    }

    @Test
    void testImportArticleArrayListFromFile() {
        // Scan a mock .csv file containing 5 sample articles
       ArrayList<NYTArticle> testArrayList = newYorkTimesClient.importArticleArrayListFromFile(file_path);

       // Verify that all 5 articles have been deserialized
       assertEquals(5, testArrayList.size());
    }

    // How to use assertThrows?
    @Test
    void emptyPathExceptionTest() {
        // Run the method with an empty file path to check if an exception is launched
        try {
            ArrayList<NYTArticle> testArrayList = newYorkTimesClient.importArticleArrayListFromFile("");
            System.out.print("No exception launched");
        }
        catch (InvalidFileFormatException e) {
            System.out.print("Exception launched correctly");
        }
    }
    
}