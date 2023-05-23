package adapters;

import models.TheGuardian.TGArticle;
import models.TheGuardian.TGFields;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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