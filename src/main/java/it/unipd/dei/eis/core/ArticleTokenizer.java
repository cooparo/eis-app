package it.unipd.dei.eis.core;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import it.unipd.dei.eis.interfaces.IArticle;

import java.util.*;

public class ArticleTokenizer {

    private final static String LEMMAS_ANNOTATORS = "tokenize,ssplit,pos,lemma";

    /**
     * Extract tokens from the input article (title and body).
     * @param article the article to tokenize.
     * @return set of distinct tokens extracted from the article, in lowercase.
     */
    public static Set<String> tokenize(IArticle article) {
        // Merge title and body
        String text = article.getTitle() + " " + article.getBody();

        // Removes punctuation
        text = text.replaceAll("[^a-zA-Z' ]", " ");

        return extractLemmas(text);

    }

    /**
     * Create a StanfordCoreNLP pipeline with the specified annotators.
     * @param annotators the annotators to use.
     * @return A StanfordCoreNLP pipeline.
     */
    private static StanfordCoreNLP createPipeline(String annotators) {
        // set up pipeline properties
        Properties props = new Properties();
        // set the list of annotators to run
        props.setProperty("annotators", annotators);
        // build pipeline
        return new StanfordCoreNLP(props);
    }

    /**
     * Extract lemmas from the input text.
     * @param text the text to analyze.
     * @return Set of distinct lemmas extracted from the text, in lowercase.
     */
    private static Set<String> extractLemmas(String text) {

        StanfordCoreNLP nlpPipeline = createPipeline(ArticleTokenizer.LEMMAS_ANNOTATORS);

        // create a document object
        CoreDocument document = new CoreDocument(text);

        // annotate the document
        nlpPipeline.annotate(document);

        Set<String> distinctLemmas = new TreeSet<>();
        for (CoreLabel token : document.tokens()) {
            String lemma = token.get(CoreAnnotations.LemmaAnnotation.class).toLowerCase();

            distinctLemmas.add(lemma);
        }

        return distinctLemmas;
    }
}
