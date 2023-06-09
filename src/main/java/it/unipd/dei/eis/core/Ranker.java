package it.unipd.dei.eis.core;

import it.unipd.dei.eis.interfaces.IArticle;
import it.unipd.dei.eis.repository.ArticleRepository;
import it.unipd.dei.eis.utils.IO;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Ranker {

    private static final String BASE_PATH = "src/main/resources/";
    private static final String STOP_LIST_PATH = BASE_PATH + "english_stoplist_v1.txt";
    private Map<String, Integer> wordFrequencyMap = new HashMap<>();

    private final ArticleRepository repository;

    public Ranker(ArticleRepository repository) {
        this.repository = repository;
    }

    /**
     * Ranks the articles by word frequency, in descending order.
     * The ranking is done in parallel, for each article.
     * Updates the wordFrequencyMap.
     */
    public Map<String, Integer> rank() {

        for (IArticle article : repository.getAll()) {
            Set<String> tokenSet = ArticleTokenizer.tokenize(article);

            for (String word : tokenSet) {
                // Check if the word is already present in the wordFrequencyMap
                boolean wordExists = false;
                for (Map.Entry<String, Integer> entry : wordFrequencyMap.entrySet()) {
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
                    wordFrequencyMap.put(word, 1);
                }
            }
        }

        clearUsingStopList();
        wordFrequencyMap = sortMapByValueThenByKey(wordFrequencyMap);
        return wordFrequencyMap;
    }

    public static Map<String, Integer> sortMapByValueThenByKey(Map<String, Integer> wordFrequencyMap) {
        // Create a compound comparator to sort by value (descending) and then by key (ascending)
        Comparator<Map.Entry<String, Integer>> valueComparator = Map.Entry.comparingByValue(Comparator.reverseOrder());
        Comparator<Map.Entry<String, Integer>> keyComparator = Map.Entry.comparingByKey();
        Comparator<Map.Entry<String, Integer>> compoundComparator = valueComparator.thenComparing(keyComparator);

        // Sorting by value (descending) and then by key (ascending)
        List<Map.Entry<String, Integer>> sortedByValueDescendingThenKeyAscending = wordFrequencyMap.entrySet()
                .stream()
                .sorted(compoundComparator)
                .collect(Collectors.toList());


        wordFrequencyMap = sortedByValueDescendingThenKeyAscending.stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        return wordFrequencyMap;
    }

    private void clearUsingStopList() { //TODO: use CoreLNP custom's stop-list
        Set<String> words = new HashSet<>();

        try {
            File file = new File(Ranker.STOP_LIST_PATH);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String word = scanner.nextLine().trim();
                words.add(word);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (String word : words) {
            wordFrequencyMap.remove(word);
        }
    }

    public Map<String, Integer> getWordFrequencyMap() {
        return wordFrequencyMap;
    }

    public String getWordFrequencyReport() { return getWordFrequencyReport(wordFrequencyMap.size()); }
    public String getWordFrequencyReport(int limit) {
        StringBuilder result = new StringBuilder();

        int index = 0;
        for (Map.Entry<String, Integer> entry : wordFrequencyMap.entrySet()) {
            if (index++ > limit) break;
            result.append(entry.getKey()).append(" ").append(entry.getValue()).append("\n");
        }
        return result.toString();
    }

    /**
     * Saves the wordFrequencyMap on a txt file.
     * @param fileName      the name of the file, e.g. "wordFrequencyMap.txt"
     * @throws IOException
     */
    public void saveOnTxt(String fileName) throws IOException {
        IO.writeFile(BASE_PATH + fileName, getWordFrequencyReport());
    }
}