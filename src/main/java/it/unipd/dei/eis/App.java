package it.unipd.dei.eis;

import it.unipd.dei.eis.adapters.TheGuardianAdapter;
import it.unipd.dei.eis.clients.remote.TheGuardianClient;
import it.unipd.dei.eis.interfaces.IArticle;
import it.unipd.dei.eis.models.Article;
import it.unipd.dei.eis.models.TheGuardian.TGArticle;
import it.unipd.dei.eis.models.TheGuardian.TGResponseWrapper;
import it.unipd.dei.eis.repository.ArticleRepository;
import it.unipd.dei.eis.utils.Format;
import it.unipd.dei.eis.utils.IO;
import it.unipd.dei.eis.utils.Marshalling;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;

public class App {
    private static final ArticleRepository repository = new ArticleRepository();

    public static void main(String[] args) throws IOException {
        download();
        System.out.println(repository.getAll().get(0).getTitle());
    }
    public static void download() throws IOException {
        TheGuardianReader();
    }
    public static PriorityQueue<Map.Entry<String, Integer>> rank(String input) {
        Map<String, Integer> wordFrequencyMap = new HashMap<>();

        // Remove all non-alphabetic characters and split the string into words
        String[] words = input.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");

        // Create a PriorityQueue to sort the words by frequency
        PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>((a, b) -> {
            int freqComparison = b.getValue().compareTo(a.getValue()); // Orders by frequency
            if (freqComparison == 0) {
                return a.getKey().compareTo(b.getKey()); // Orders alphabetically
            }
            return freqComparison;
        });

        // Counts the frequency of each word
        for (String word : words) {
            wordFrequencyMap.put(word, wordFrequencyMap.getOrDefault(word, 0) + 1);
        }
        pq.addAll(wordFrequencyMap.entrySet());

        return pq;
    }

    public static void TheGuardianDownloader() throws IOException {
        final String API_KEY = "API_KEY";
        TheGuardianClient client = new TheGuardianClient(API_KEY);

        ArrayList<TGArticle> articles = client.getArticleArrayList("bitcoin", 12);

        addDataToRepository(articles, (article) -> new TheGuardianAdapter(article));
    }
    public static void TheGuardianReader() throws IOException {
        String text = IO.readFile("./src/main/resources/the_guardian_response_1.json");
        TGResponseWrapper root = Marshalling.deserialize(Format.JSON, text, TGResponseWrapper.class);

        ArrayList<TGArticle> articles = root.getResponse().getResults();
        addDataToRepository(articles, (article) -> new TheGuardianAdapter(article));
    }

    private static <NewspaperArticle> void addDataToRepository(ArrayList<NewspaperArticle> articles, Function<NewspaperArticle, IArticle> converter) throws IOException {
        for (NewspaperArticle article : articles) {
            repository.add(new Article(converter.apply(article)));
        }
        repository.saveToDisk();
    }
}