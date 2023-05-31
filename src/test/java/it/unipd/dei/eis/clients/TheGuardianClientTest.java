package it.unipd.dei.eis.clients;

import it.unipd.dei.eis.clients.remote.TheGuardianClient;
import it.unipd.dei.eis.models.TheGuardian.TGArticle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TheGuardianClientTest {
        private TheGuardianClient theGuardianClient;

    @BeforeEach
    void setUp() {
        // Set up the TheGuardianClient instance with your API key
        final String API_KEY = System.getenv("API_KEY");
        theGuardianClient = new TheGuardianClient(API_KEY);
    }

    @Test
    void getArticleArrayList_ValidQueryAndNumber_ReturnsArticleList() throws IOException {
        // Arrange
        String query = "bitcoin";
        int articlesNumber = 20;

        // Act
        ArrayList<TGArticle> articleList = null;
        try {
            articleList = theGuardianClient.getArticleArrayList(query, articlesNumber);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Assert
        assertNotNull(articleList);
        assertEquals(articlesNumber, articleList.size());
    }
}