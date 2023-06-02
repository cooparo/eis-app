package it.unipd.dei.eis.adapters;

import it.unipd.dei.eis.models.TheGuardian.TGArticle;
import it.unipd.dei.eis.models.TheGuardian.TGFields;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TheGuardianAdapterTest {

    private TheGuardianAdapter theGuardianAdapter;
    private TGArticle testArticle;

    @BeforeEach
    void setUp() {
        // Set up a test TGArticle object
        String webTitle = "Test Article";
        String bodyText = "This is the test article's body text";
        TGFields fields = new TGFields();
        fields.setBodyText(bodyText);
        testArticle = new TGArticle();
        testArticle.setWebTitle(webTitle);
        testArticle.setFields(fields);

        // Create the TheGuardianAdapter instance
        theGuardianAdapter = new TheGuardianAdapter(testArticle);
    }

    @Test
    public void getId_ReturnsArticleId() {
        // Arrange
        String expectedTitle = "Test Article";

        // Act
        String actualTitle = theGuardianAdapter.getTitle();

        // Assert
        assertNotNull(actualTitle);
        assertEquals(expectedTitle, actualTitle);
    }

    @Test
    public void getTitle_ReturnsWebTitle() {
        // Arrange
        String expectedId = "1234567";

        // Act
        String actualId = theGuardianAdapter.getId();

        // Assert
        assertNotNull(actualId);
        assertEquals(expectedId, actualId);
    }

    @Test
    public void getBody_ReturnsBodyText() {
        // Arrange
        String expectedBody = "This is the test article's body text";

        // Act
        String actualBody = theGuardianAdapter.getBody();

        // Assert
        assertNotNull(actualBody);
        assertEquals(expectedBody, actualBody);
    }

    @Test
    public void getPublicationTime_ReturnsPublicationTime() {
        // Arrange
        Instant expectedDate = Instant.parse("2017-02-03T10:37:30.00Z");

        // Act
        Instant actualDate = theGuardianAdapter.getPublicationTime();

        // Assert
        assertNotNull(actualDate);
        assertEquals(expectedDate, actualDate);
    }
}