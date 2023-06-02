package it.unipd.dei.eis.adapters;

import it.unipd.dei.eis.models.NewYorkTimes.NYTArticle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

class NewYorkTimesAdapterTest {

    @Test
    public void testGetId() {
        // Create a mock NYTArticle object with a specific identifier
        NYTArticle nytArticle = new NYTArticle();
        nytArticle.setIdentifier("123456");

        // Create an instance of NewYorkTimesAdapter using the mock NYTArticle
        NewYorkTimesAdapter adapter = new NewYorkTimesAdapter(nytArticle);

        // Verify that the getId() method returns the expected identifier
        Assertions.assertEquals("123456", adapter.getId());
    }

    @Test
    public void testGetTitle() {
        // Create a mock NYTArticle object with a specific title
        NYTArticle nytArticle = new NYTArticle();
        nytArticle.setTitle("Sample Article Title");

        // Create an instance of NewYorkTimesAdapter using the mock NYTArticle
        NewYorkTimesAdapter adapter = new NewYorkTimesAdapter(nytArticle);

        // Verify that the getTitle() method returns the expected title
        Assertions.assertEquals("Sample Article Title", adapter.getTitle());
    }

    @Test
    public void testGetBody() {
        // Create a mock NYTArticle object with a specific body
        NYTArticle nytArticle = new NYTArticle();
        nytArticle.setBody("Sample article body content");

        // Create an instance of NewYorkTimesAdapter using the mock NYTArticle
        NewYorkTimesAdapter adapter = new NewYorkTimesAdapter(nytArticle);

        // Verify that the getBody() method returns the expected body content
        Assertions.assertEquals("Sample article body content", adapter.getBody());
    }

    @Test
    public void testGetPublicationTime() {
        // Create a mock NYTArticle object with a specific date
        NYTArticle nytArticle = new NYTArticle();
        nytArticle.setDate(Instant.parse("2017-02-03T10:37:30.00Z"));

        // Create an instance of NewYorkTimesAdapter using the mock NYTArticle
        NewYorkTimesAdapter adapter = new NewYorkTimesAdapter(nytArticle);

        // Verify that the getDate() method returns the expected body content
        Assertions.assertEquals("Sample article body content", adapter.getBody());
    }

}