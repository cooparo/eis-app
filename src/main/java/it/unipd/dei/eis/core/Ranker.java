package it.unipd.dei.eis.core;

import it.unipd.dei.eis.interfaces.IArticle;
import it.unipd.dei.eis.repository.ArticleRepository;
import it.unipd.dei.eis.utils.IO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Ranker {

    private static final String BASE_PATH = "src/main/resources/";
    private static final String STOP_LIST_PATH = BASE_PATH + "english_stoplist_v1.txt";
    private Map<String, Integer> wordFrequencyMap = new ConcurrentHashMap<>();
    private final Set<String> STOP_LIST = new HashSet<>(loadStopList());

    private final ArticleRepository repository;

    public Ranker(ArticleRepository repository) {
        this.repository = repository;
    }

    /**
     * Ranks the articles by word frequency, in descending order.
     * The ranking is done in parallel, for each article.
     * Updates the wordFrequencyMap.
     *
     * @return the wordFrequencyMap, in the format 'word 'count'"
     */
    public Map<String, Integer> rank() {

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        // For each article, tokenize it and update the wordFrequencyMap
        for (IArticle article : repository.getAll()) {

            // Starts a thread for each article
            executorService.execute(() -> {
                Set<String> tokenSet = ArticleTokenizer.tokenize(article);

                for (String word : tokenSet) {
                    // Check if the word is in the stop list
                    if (isStopList(word)) continue;
                    // Check if the word is already present in the wordFrequencyMap (previous articles)
                    Integer value = wordFrequencyMap.get(word);

                    if (value == null) value = 0;
                    wordFrequencyMap.put(word, value + 1);
                }
            });

        }
        executorService.shutdown();
        try {
            // Wait until all threads are finish
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        wordFrequencyMap = sortMapByValueThenByKey(wordFrequencyMap);

        return wordFrequencyMap;
    }

    /**
     * Sorts the Map by value (descending) and then by key (ascending).
     *
     * @param wordFrequencyMap the map to be sorted
     * @return the sorted map
     */
    public static Map<String, Integer> sortMapByValueThenByKey(Map<String, Integer> wordFrequencyMap) {

        // Create a compound comparator to sort by value (descending) and then by key (ascending)
        Comparator<Map.Entry<String, Integer>> valueComparator = Map.Entry.comparingByValue(Comparator.reverseOrder());
        Comparator<Map.Entry<String, Integer>> keyComparator = Map.Entry.comparingByKey();
        Comparator<Map.Entry<String, Integer>> compoundComparator = valueComparator.thenComparing(keyComparator);

        // Sorting by value (descending) and then by key (ascending)
        wordFrequencyMap = wordFrequencyMap.entrySet()
                .stream()
                .sorted(compoundComparator)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return wordFrequencyMap;
    }

    /**
     * Removes the words in the stop list from the wordFrequencyMap.
     */
    public boolean isStopList(String word) {
        return STOP_LIST.contains(word);
    }

    private Set<String> loadStopList() {
        Set<String> stopList = new HashSet<>();
        try {
            File file = new File(Ranker.STOP_LIST_PATH);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String word = scanner.nextLine().trim();
                stopList.add(word);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return stopList;
    }

    public Map<String, Integer> getWordFrequencyMap() {
        return wordFrequencyMap;
    }

    /**
     * Returns a string containing the word frequency report. The number of words shown is equal to wordFrequencyMap.size().
     *
     * @return the word frequency report, in the format 'word count'"
     */
    public String getWordFrequencyReport() {
        return getWordFrequencyReport(wordFrequencyMap.size());
    }

    /**
     * Returns a string containing the word frequency report.
     *
     * @param limit the number of words to be shown.
     * @return the word frequency report, in the format 'word' 'count'"
     */
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
     * Saves the wordFrequencyMap in a txt file.
     *
     * @param fileName the name of the file, e.g. "wordFrequencyMap.txt"
     * @throws IOException if an I/O error occurs
     */
    public void saveOnTxt(String fileName) throws IOException {
        IO.writeFile(BASE_PATH + fileName, getWordFrequencyReport());
    }
}