package it.unipd.dei.eis.newspapers.TheGuardian;

import it.unipd.dei.eis.newspapers.TheGuardian.models.TGArticle;
import it.unipd.dei.eis.newspapers.TheGuardian.models.TGFields;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TGArticleAdapterTest {

    private TGArticleAdapter theGuardianAdapter;
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
        theGuardianAdapter = new TGArticleAdapter(testArticle);
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

}