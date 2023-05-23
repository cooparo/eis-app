package client;

import models.TheGuardian.TGArticle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void getArticleArrayList_ValidQueryAndNumber_ReturnsArticleList() {
        // Arrange
        String query = "bitcoin";
        int articlesNumber = 20;

        // Act
        ArrayList<TGArticle> articleList = theGuardianClient.getArticleArrayList(query, articlesNumber);

        // Assert
        assertNotNull(articleList);
        assertEquals(articlesNumber, articleList.size());
    }
}