package it.unipd.dei.eis.newspapers.TheGuardian;

import it.unipd.dei.eis.newspapers.TheGuardian.models.TGArticle;
import it.unipd.dei.eis.newspapers.TheGuardian.models.TGFields;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Instant;

class TGArticleAdapterTest {

    private TGArticleAdapter theGuardianAdapter;
    private TGArticle testArticle;

    @BeforeEach
    void setUp() {
        // Set up a test TGArticle object
        String id = "Test Id";
        String webTitle = "Test Article";
        String bodyText = "This is the test article's body text";
        Instant publicationTime = Instant.parse("2017-02-03T10:37:30.00Z");

        TGFields fields = new TGFields();
        fields.setBodyText(bodyText);

        testArticle = new TGArticle();
        testArticle.setId(id);
        testArticle.setWebTitle(webTitle);
        testArticle.setFields(fields);
        testArticle.setWebPublicationDate(publicationTime);

        // Create the TheGuardianAdapter instance
        theGuardianAdapter = new TGArticleAdapter(testArticle);
    }

    @Test
    void getId_ReturnsId() {
        // Arrange
        String expectedId = "Test Id";

        // Act
        String actualId = theGuardianAdapter.getId();

        // Assert
        assertNotNull(actualId);
        assertEquals(expectedId, actualId);
    }

    @Test
    void getTitle_ReturnsWebTitle() {
        // Arrange
        String expectedTitle = "Test Article";

        // Act
        String actualTitle = theGuardianAdapter.getTitle();

        // Assert
        assertNotNull(actualTitle);
        assertEquals(expectedTitle, actualTitle);
    }

    @Test
    void getBody_ReturnsBodyText() {
        // Arrange
        String expectedBody = "This is the test article's body text";

        // Act
        String actualBody = theGuardianAdapter.getBody();

        // Assert
        assertNotNull(actualBody);
        assertEquals(expectedBody, actualBody);
    }

    @Test
    void getPublicationTime_ReturnsPublicationTime() {
        // Arrange
        Instant expectedPublicationTime = Instant.parse("2017-02-03T10:37:30.00Z");

        // Act
        Instant actualPublicationTime = theGuardianAdapter.getPublicationTime();

        // Assert
        assertNotNull(actualPublicationTime);
        assertEquals(expectedPublicationTime, actualPublicationTime);
    }

}