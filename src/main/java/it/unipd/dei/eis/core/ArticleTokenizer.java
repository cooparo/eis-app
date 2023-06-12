package it.unipd.dei.eis.core;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import it.unipd.dei.eis.interfaces.IArticle;

import java.util.*;

public class ArticleTokenizer {

    // Part-of-Speech (POS) tag che rappresentano sostantivi (noun)
    private final static Set<String> NOUN_POS_TAGS = new TreeSet<String>() {{
        // ENGLISH
        add("NN");  // noun, singular or mass
        add("NNS"); // noun, plural
        add("NNP"); // proper noun, singular
        add("NNPS"); // proper noun, plural
    }};

    private final static String TOKENS_ANNOTATORS = "tokenize,ssplit,pos";
    private final static String LEMMAS_ANNOTATORS = "tokenize,ssplit,pos,lemma";
    private final static String NOUNS_ANNOTATORS = "tokenize,ssplit,pos";

    /**
     * Extract tokens from the input article (title and body)
     * @param article   the article to tokenize
     * @return set of distinct tokens extracted from the article, in lowercase
     */
    public static Set<String> tokenize(IArticle article) {
        // Merge title and body
        String text = article.getTitle() + " " + article.getBody();

        //TODO: se rimuovo la puntaggiato funziona la lemmizzazione?
        // Removes punctuation
        text = text.replaceAll("[^a-zA-Z ]", "").toLowerCase();

        Set<String> tokens = new TreeSet<>();

        // TODO: mettiamo tutto o no?
        // Add all the tokens to the set
        tokens.addAll(extractTokens(text));
        // Add all the lemmas to the set
        tokens.addAll(extractLemmas(text));
        // Add all the nouns to the set
        tokens.addAll(extractNouns(text));

        return tokens;

    }

    /**
     * Create a StanfordCoreNLP pipeline with the specified annotators
     * @param annotators   the annotators to use
     * @return a StanfordCoreNLP pipeline
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
     * Estrae i token dal testo in input, utilizzando la sequenza di annotatori specificata.
     *
     * @param text il testo da annotare
     * @return insieme dei token distinti estratti dal testo, in minuscolo
     * @since 0.1
     */
    private static Set<String> extractTokens(String text) {

        StanfordCoreNLP nlpPipeline = createPipeline(ArticleTokenizer.TOKENS_ANNOTATORS);

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
     * Estrae i lemma dal testo in input, utilizzando la sequenza di annotatori specificata.
     *
     * @param text il testo da annotare
     * @return insieme dei lemma distinti estratti dal testo, in minuscolo
     * @since 0.1
     */
    private static Set<String> extractLemmas(String text) {

        StanfordCoreNLP nlpPipeline = createPipeline(ArticleTokenizer.LEMMAS_ANNOTATORS);

        // create a document object
        CoreDocument document = new CoreDocument(text);

        // annotate the document
        nlpPipeline.annotate(document);

        Set<String> distinctLemmas = new TreeSet<>();
        // tokens
        for (CoreLabel token : document.tokens()) {

            String lemma = token.get(CoreAnnotations.LemmaAnnotation.class).toLowerCase();

            distinctLemmas.add(lemma);
        }

        return distinctLemmas;
    }

    /**
     * Estrae i sostantivi dal testo in input, utilizzando la sequenza di annotatori specificata.
     *
     * @param text il testo da annotare
     * @return insieme dei sostantivi distinti estratti dal testo, in minuscolo
     * @since 0.1
     */
    private static Set<String> extractNouns(String text) {

        StanfordCoreNLP nlpPipeline = createPipeline(ArticleTokenizer.NOUNS_ANNOTATORS);

        // create a document object
        CoreDocument document = new CoreDocument(text);
        // annotate the document
        nlpPipeline.annotate(document);

        Set<String> distinctNouns = new TreeSet<>();

        // tokens
        for (CoreLabel token : document.tokens()) {

            // this is the text of the token
            String word = token.get(CoreAnnotations.TextAnnotation.class);
            // this is the POS tag of the token
            String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);

            if (NOUN_POS_TAGS.contains(pos.toUpperCase())) {
                distinctNouns.add(word.toLowerCase());
            }

        }

        return distinctNouns;
    }


}
