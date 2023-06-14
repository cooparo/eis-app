package it.unipd.dei.eis.core;

import it.unipd.dei.eis.interfaces.IArticle;
import it.unipd.dei.eis.repository.Article;
import it.unipd.dei.eis.repository.ArticleRepository;
import it.unipd.dei.eis.utils.FileFormat;
import it.unipd.dei.eis.utils.IO;
import it.unipd.dei.eis.utils.Marshalling;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RankerTest {

    @Nested
    class DiskLoading {

        private Ranker ranker;
        private final ArticleRepository repository = new ArticleRepository();
        private final static String STORAGE_TEST_PATH_100_ARTICLES = "./src/test/resources/100_articles.json";
//        private static final String STORAGE_TEST_PATH_1000_ARTICLES = "src/test/resources/1000_articles.json";

        private ArrayList<Article> loadFromDiskTest(String fileName) {
            String serializedArticles;
            try {
                serializedArticles = IO.readFile(fileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return Marshalling.deserialize(FileFormat.JSON, serializedArticles, new Marshalling.TypeReference<ArrayList<Article>>() {
            });
        }

        void setUp(String fileName) {
            repository.removeAll();
            ArrayList<Article> articles = loadFromDiskTest(fileName);
            repository.add(articles.toArray(new Article[0]));

            ranker = new Ranker(repository);
        }

        @Test
        void rankTest100Articles() {
            setUp(STORAGE_TEST_PATH_100_ARTICLES);
            Map<String, Integer> results = ranker.rank();

//            System.out.println("bitcoin: " + results.get("bitcoin"));

            assertNotNull(results);
            assertTrue(results.get("bitcoin") > 80);
        }

//        @Test
//        void rankTest1000Articles() {
//            setUp(STORAGE_TEST_PATH_1000_ARTICLES);
//            Map<String, Integer> results = ranker.rank();
//
////            System.out.println("bitcoin: " + results.get("bitcoin"));
//
//            assertNotNull(results);
//            assertTrue(results.get("bitcoin") > 800);
//        }
    }


    @Nested
    class InlineLoading {

        Ranker ranker;
        private final ArticleRepository repository = new ArticleRepository();

        void setUp() {
            IArticle article1 = new Article(
                    "01",
                    "Trump lawyer said to have been waved off searching office for secret records",
                    "",
                    null
            );
            IArticle article2 = new Article(
                    "02",
                    "US supreme court end-of-term decisions could transform key areas of public life",
                    "A sharp rise in bitcoin prices has pushed the cryptocurrency above $30,000 (£24,118) for the first time since 10 June last year, just before the Celsius crypto lending company froze withdrawals in the run-up to its collapse. Even given that recovery, the token is still well below its all-time high of $68,000 in November 2021, and far below where it was before the failure of the Terra stablecoin caused the “crypto winter”. Nevertheless, bitcoin’s recent steady increase in value has sparked discussion of another cryptocurrency boom – and reignited fears of widespread manipulation in the market. The collapse of Silicon Valley Bank last month and the broader contagion it has sparked across financial markets led some cryptocurrency fans to turn to bitcoin, the original and most valuable token in the sector, as a way of protecting against fears that the entire traditional “fiat” economy would crumble. That attitude was typified by the US venture capitalist Balaji Srinivasan, who in March bet $1m that the price of a single bitcoin would top $1m by June this year. His claim was that the US dollar would shortly experience hyperinflation, causing the dollar value of a bitcoin to soar. “This is the moment that the world redenominates on bitcoin as digital gold, returning to a model much like before the 20th century,” he tweeted, explaining the bet. “Everything will happen very fast once people check what I’m saying and see that the Federal Reserve has lied about how much money there is in the banks. All dollar holders get destroyed.” Alex Adelman, the chief executive of the bitcoin rewards app Lolli, said Monday’s rally “did not have a clear catalyst”, but that it was “a bellwether of bitcoin’s newly bullish market conditions and strong investor confidence. Bitcoin’s ongoing strength suggests that bitcoin is emerging from so-called ‘crypto winter’ into a new phase of strength and renewed interest from retail and institutional investors.” But the recovery, after bitcoin prices hovered at $28,000 for almost a month before leaping the final $2,000 in a day, has also led to concern about market manipulation. A 2022 report published by the US National Bureau of Economic Research found that “wash trading”, the practice of selling cryptocurrencies between related parties to influence the reported price, averaged “over 70% of the reported volume” on 29 unregulated exchanges. In June 2022, the US Securities and Exchange Commission (SEC) refused permission to launch a bitcoin-linked exchange traded fund, which would allow investors to buy exposure to the cryptocurrency on the public stock markets, after concluding that it was impossible to prevent fraud and manipulation in the market from affecting the price. As well as wash trading, the SEC said the market could be influenced by individuals with a “dominant position” in bitcoin manipulating its pricing, through fraud and manipulation at trading platforms, and through manipulative activity involving stablecoins “including tether”.",
                    null
            );
            IArticle article3 = new Article(
                    "03",
                    "Theranos founder Elizabeth Holmes expected to begin 11-year sentence",
                    "The amount of electricity consumed by the largest cryptocurrency networks has decreased by up to 50% as the “crypto winter” continues to eat at the incomes of “miners” and financial contagion spreads further throughout the sector. The electricity consumption of the bitcoin network has fallen by a third from its high of 11 June, down to an annualised 131 terawatt-hours a year, according to estimates from the crypto analyst Digiconomist. That still equates to the annual consumption of Argentina, with a single conventional bitcoin transaction using the same amount of electricity that a typical US household would use over 50 days. The decrease in electricity used for Ethereum, the “programmable money” that underpins much of the recent explosion in crypto projects, has been sharper still, down from a peak of 94TWh a year to 46TWh a year – the annualised consumption of Qatar. The underlying reason for the fall is the same for both currencies, however. The electricity consumption of a cryptocurrency network comes from “mining”, which involves people using purpose-built computers to generate digital lottery tickets that can reward cryptocurrency payouts. The process underpins the security of the networks, but incentivises the network as a whole to waste extraordinary amounts of energy. As the price of cryptocurrencies has fallen – bitcoin peaked at $69,000 (£56,000) earlier this year, and is now hovering at about $20,000 – the value of the rewards to miners has dropped by the same proportion, leaving those in areas with expensive electricity or using older, inefficient mining “rigs” unable to turn a profit. “This is literally putting them out of business, starting with the ones that operate with suboptimal equipment or under suboptimal circumstances (eg inefficient cooling),” said Alex de Vries, the Dutch economist behind Digiconomist. “For bitcoin mining equipment that’s a big issue, because those machines cannot be repurposed to do something else. When they’re unprofitable they’re useless machines. You can keep them around hoping the price will recover or sell them for scrap.” Ethereum, by contrast, can be mined using a normal computer. But it is most profitable to do so using a very powerful graphics card, which has led to widespread supply shortages of the cards and turned many gamers against the industry. The collapse in mining revenue has led to a flood of graphics cards on the second-hand market, as insolvent miners try to recoup their investments, but De Vries warnsit is a lottery to buy one. “These machines are typically operating 24/7 and the components will get hot doing so. Heat [especially for prolonged periods of time] is known to wear out electronics, reducing longevity and reliability. “Right now it will mainly be older GPUs [graphics processing unit] becoming unprofitable, meaning that it’s not unlikely these devices have been used for mining for a long time.” Thankfully for gamers, the falling demand has also led to large price cuts for new components. Although the fall in bitcoin’s price has stabilised over the past week, the wider cryptocurrency sector continues to stumble as a result of the huge price collapse. The latest jolt was caused by the failure of the ersatz cryptobank Celsius, which announced on 12 June that it was halting withdrawals as it faced a liquidity crisis. The failure of Celsius triggered a domino effect across the wider sector: Three Arrows Capital (3AC), a multibillion-dollar hedge fund, experienced its own liquidity crunch as a result, and multiple companies with substantial outstanding loans to 3AC have now had to take emergency measures in turn. Two other companies that offered bank-like services announced large exposures to 3AC. Last week Finblox said the hedge fund’s actions had an “effect on liquidity”, and heavily restricted user withdrawals, dropping the daily limit from $50,000 to $500 while stopping interest payments on deposits. On Wednesday Voyager, which offers 12% on crypto deposits, revealed it had an outstanding loan of $650m to 3AC, more than four times its available cash. Voyager added that it would consider 3AC in default if the hedge fund did not repay the loan in full by Monday morning. The company has also reportedly frozen user withdrawals. Bancor, a decentralised finance protocol that acts as an exchange, lost out to “the recent insolvency of two large centralised entities”, believed to be Celsius and 3AC, and had to impose withdrawal limits. On Thursday another crypto exchange, CoinFLEX, announced that it was pausing withdrawals because of “extreme market conditions”. Amid the collapses, one large cryptocurrency company has emerged as a would-be saviour of the sector. Alameda Ventures, the investment arm of the crypto entrepreneur Sam Bankman-Fried’s empire, centred on his exchange FTX, has bailed out Voyager and the embattled exchange BlockFi, offering multimillion-dollar loans to both companies. The loans have earned him comparisons to JP Morgan, the US banker who stepped in during a 1907 financial crisis and bought up the stock of troubled companies in an effort to halt the collapse.",
                    null
            );
            repository.removeAll();
            repository.add(article1, article2, article3);
            ranker = new Ranker(repository);

        }

        @Test
        void rankTest() {
            setUp();
            Map<String, Integer> results = ranker.rank();

            assertNotNull(results);

        }
    }

}