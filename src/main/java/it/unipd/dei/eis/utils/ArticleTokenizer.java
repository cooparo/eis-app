package it.unipd.dei.eis.utils;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import it.unipd.dei.eis.models.Article;

import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

public class ArticleTokenizer {


    private static final String ANNOTATORS = "tokenize,ssplit,pos";
    /**
     * Extract tokens from the input article (title and body), using the specified annotators sequence.
     * @param article   the article to tokenize
     * @return set of distinct tokens extracted from the article, in lowercase
     */
    public static Set<String> tokenize(Article article) {
        StanfordCoreNLP nlpPipeline = createPipeline(ANNOTATORS);

        String text = article.getTitle() + "\n" + article.getBody();

        // create a document object
        CoreDocument document = new CoreDocument(text);

        // annotate the document
        nlpPipeline.annotate(document);

        Set<String> distinctTokens = new TreeSet<>();
        // tokens
        for (CoreLabel token : document.tokens()) {

            String tkn = token.originalText().toLowerCase();

            distinctTokens.add(tkn);
        }

        return distinctTokens;
    }

    /**
     * Create a StanfordCoreNLP pipeline with the specified annotators sequence.
     * @param annotators
     * @return
     */
    public static StanfordCoreNLP createPipeline(String annotators) {
        Properties props = new Properties();
        props.setProperty("annotators", annotators);
        return new StanfordCoreNLP(props);
    }

}
