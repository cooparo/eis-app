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
    private Article articleTarget1;
    private String query;

    @BeforeEach
    void setUp() {
        Dotenv dotenv = Dotenv.configure().filename("api.env").load();
        theGuardianClient = new TGClient(dotenv.get("THE_GUARDIAN_API_KEY"));

        articlesNumber = 1000;
        query = "nuclear power";

        articleTarget1 = new Article(
                "news/2023/may/12/weatherwatch-concerns-over-climate-impact-on-uk-nuclear-power-sites",
                "Weatherwatch: concerns over climate impact on UK nuclear power sites",
                "Successive governments since the 1980s have had plans for new generations of nuclear power stations sited around the coasts of the United Kingdom. Although the main reason for building them, according to politicians, is to provide a low-carbon form of electricity to combat the climate crisis, no thought seems to have gone into what the climate crisis might do to the nuclear power stations. Prof Andy Blowers, a former government adviser on nuclear waste, points out in the Town and Country Planning Association Journal that the eight sites identified in 2011 as suitable for new stations are the same as those identified half a century earlier, on which the first generation of nuclear power stations were built. The reason the sites were originally chosen was their remoteness, for safety, and their proximity to the sea, for cooling purposes. The latest reasoning is that they would have a better chance of public acceptance because two generations of local people have worked in the industry. The new installations are planned to operate for 60 years and will need another century after closure to cool sufficiently to remove the waste. Blowers, an opponent of the government plans, worries that ministers seem to have taken no account of sea level rise, intense storms and the prospect of flooding at these sites.",
                null
        );
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