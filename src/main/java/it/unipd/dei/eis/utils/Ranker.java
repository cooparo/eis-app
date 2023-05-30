package it.unipd.dei.eis.utils;

import it.unipd.dei.eis.models.Article;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Ranker {
    private PriorityQueue<Map.Entry<String, Integer>> wordFrequencyMap;

    public Ranker() {
        this.wordFrequencyMap = new PriorityQueue<>(
                (a, b) -> {
            int freqComparison = b.getValue().compareTo(a.getValue()); // Orders by frequency
            if (freqComparison == 0) {
                return a.getKey().compareTo(b.getKey()); // Orders alphabetically
            }
            return freqComparison;
        });
    }

    /**
     * Ranks the articles by word frequency, in descending order.
     * The ranking is done in parallel, for each article.
     * Updates the wordFrequencyMap.
     * @param articles  the articles to be ranked
     */
    public void rank(ArrayList<Article> articles) {

        for (Article article : articles) {
            Set<String> tokenSet = ArticleTokenizer.tokenize(article);

            for (String word : tokenSet) {
                // Check if the word is already present in the wordFrequencyMap
                boolean wordExists = false;
                for (Map.Entry<String, Integer> entry : wordFrequencyMap) {
                    if (entry.getKey().equals(word)) {
                        // Word is already present, increment the counter
                        int frequency = entry.getValue();
                        entry.setValue(frequency + 1);
                        wordExists = true;
                        break;
                    }
                }

                // Word is not present, add it to the wordFrequencyMap
                if (!wordExists) {
                    wordFrequencyMap.add(new AbstractMap.SimpleEntry<>(word, 1));
                }

            }
        }

        clearUsingStoplist();
    }

    private void clearUsingStoplist() {
        //TODO: implement
    }

    /**
     * Saves the wordFrequencyMap on a txt file.
     * @param fileName      the name of the file
     * @throws IOException
     */
    public void saveOnTxt(String fileName) throws IOException {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, Integer> entry : wordFrequencyMap) {
            result.append(entry.getKey()).append(" ").append(entry.getValue()).append("\n");
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(result.toString());

        writer.close();

        //TODO: choose directory where to save the file
    }

    public PriorityQueue<Map.Entry<String, Integer>> getWordFrequencyMap() {
        return wordFrequencyMap;
    }

}
