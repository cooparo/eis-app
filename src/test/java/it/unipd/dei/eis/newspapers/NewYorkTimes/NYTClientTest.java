package it.unipd.dei.eis.newspapers.NewYorkTimes;

import it.unipd.dei.eis.newspapers.NewYorkTimes.models.NYTArticle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class NYTClientTest {


    private static final String PATH_FILE = "src/test/resources/nytimes_articles_v2.csv";
    NYTClient client;
    @BeforeEach
    void setUp() {
        client = new NYTClient();
    }

    @Test
    void importArticleArrayListFromFile() {

        ArrayList<NYTArticle> results = client.importArticleArrayListFromFile(PATH_FILE);

        assertNotNull(results);

    }
}