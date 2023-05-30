package it.unipd.dei.eis.utils;

import it.unipd.dei.eis.models.Article;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class ArticleTokenizer {


    /**
     * Extract tokens from the input article (title and body)
     * @param article   the article to tokenize
     * @return set of distinct tokens extracted from the article, in lowercase
     */
    public static Set<String> tokenize(Article article) {
        // Merge title and body
        String text = article.getTitle() + " " + article.getBody();

        Set<String> tokens = new TreeSet<>();

        // Add all the tokens to the set
        tokens.addAll(
                // Split the text by spaces and punctuation
                Arrays.asList(text.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+"))
        );

        return tokens;

    }

}
