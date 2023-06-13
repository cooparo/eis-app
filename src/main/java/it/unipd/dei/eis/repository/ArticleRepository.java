package it.unipd.dei.eis.repository;

import it.unipd.dei.eis.interfaces.IArticle;
import it.unipd.dei.eis.interfaces.IRepository;
import it.unipd.dei.eis.interfaces.ISpecification;
import it.unipd.dei.eis.utils.FileFormat;
import it.unipd.dei.eis.utils.IO;
import it.unipd.dei.eis.utils.Marshalling;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The repository where all articles are saved.
 */
public class ArticleRepository implements IRepository<IArticle> {

    private final ArrayList<IArticle> storage;
    private FileFormat fileFormat;
    private final static String BASE_PATH = "./src/test/resources/";

    public ArticleRepository() {
        this(FileFormat.JSON);
    }

    public ArticleRepository(FileFormat fileFormat) {
        this.fileFormat = fileFormat;
        storage = ArticleStorage.getInstance().getArticleList();
    }

    public FileFormat getFileFormat() { return fileFormat; }
    public void setFileFormat(FileFormat format) { this.fileFormat = format; }

    /**
     * Adds one or more articles to the repository.
     * @param articles The articles to be added in the repository.
     */
    public void add(IArticle... articles) {
        storage.addAll(Arrays.stream(articles).map(Article::new).collect(Collectors.toList()));
    }

    /**
     * The generified form of <code>add</code> method.
     * Converts, using the specified function, all articles of a foreign class to articles of IArticle interface,
     * then adds them in the repository.
     * @param articles The ArrayList of foreign articles to be added in the repository.
     * @param converter The functions that converts a foreign article in an article of type IArticle.
     * @param <NewspaperArticle> The foreign article type.
     */
    public <NewspaperArticle> void add(ArrayList<NewspaperArticle> articles, Function<NewspaperArticle, IArticle> converter) {
        for (NewspaperArticle article : articles) {
            add(new Article(converter.apply(article)));
        }
    }

    /**
     * Returns the underlying ArrayList that holds the articles in this repository.
     */
    @Override
    public ArrayList<IArticle> getAll() {
        return storage;
    }

    /**
     * Updates an article in the repository.
     */
    @Override
    public void update(IArticle oldArticle, IArticle newArticle) {
        int oldArticleIndex = storage.indexOf(oldArticle);
        if (oldArticleIndex < 0) throw new IllegalArgumentException("Article not found");

        storage.set(oldArticleIndex, newArticle);
    }

    /**
     * Removes from the repository the specified article.
     * @param article The article to be removed.
     */
    @Override
    public void remove(IArticle article) {
        storage.remove(article);
    }

    /**
     * Returns the number of the articles contained in the repository.
     */
    @Override
    public void removeAll() {
        storage.clear();
    }

    @Override
    public int size() { return storage.size(); }

    @Override
    public ArrayList<IArticle> FindBySpecification(ISpecification<IArticle> spec) {
        ArrayList<IArticle> articleArrayList = new ArrayList<>();

        for(IArticle article : storage) {
            if(spec.exist(article)) articleArrayList.add(article);
        }

        return articleArrayList;
    }

    // PERSISTENCE

    /**
     * Returns the path to the file where the articles in this repository are stored.
     * For example: <i>src/main/resources/storage.json</i>
     */
    private String getFilePath() {
        return BASE_PATH + "storage." + fileFormat.toString().toLowerCase();
    }

    /**
     * Saves to disk the content of the repository. <p>
     * The file <i>storage.[ext]</i> will be placed in <i>src/main/resources</i>.
     * @throws IOException If an I/O error occurs
     */
    public void saveToDisk() throws IOException {
        String serializedStorage = Marshalling.serialize(fileFormat, storage);
        IO.writeFile(getFilePath(), serializedStorage);
    }

    /**
     * Loads from disk a previously saved file with the content of the repository. <p>
     * The <i>storage.[ext]</i> file will be searched in <i>src/main/resources</i> directory.
     * @throws IOException If an I/O error occurs
     */
    public void loadFromDisk() throws IOException {
        String serializedStorage = IO.readFile(getFilePath());
        ArrayList<Article> container = Marshalling.deserialize(fileFormat, serializedStorage, new Marshalling.TypeReference<ArrayList<Article>>(){});
        add(container.toArray(new IArticle[0]));
    }
}
