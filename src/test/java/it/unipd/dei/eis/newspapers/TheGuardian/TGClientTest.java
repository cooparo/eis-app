package it.unipd.dei.eis.newspapers.TheGuardian;

import io.github.cdimascio.dotenv.Dotenv;
import it.unipd.dei.eis.newspapers.TheGuardian.models.TGArticle;
import it.unipd.dei.eis.repository.Article;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TGClientTest {
    private TGClient theGuardianClient;
    private int articlesNumber;
    private String query;

    @BeforeEach
    void setUp() {
        Dotenv dotenv = Dotenv.configure()
                .directory("./src/test/resources")
                .filename("env")
                .load();
        theGuardianClient = new TGClient(dotenv.get("THE_GUARDIAN_API_KEY"));

        articlesNumber = 1000;
        query = "nuclear power";
    }

    @Test
    void getArticleArrayListTest() {

        ArrayList<TGArticle> articleList;
        articleList = theGuardianClient.getArticleArrayList(query, articlesNumber);

        // Assert
        assertNotNull(articleList);
        assertEquals(articlesNumber, articleList.size());
    }
}