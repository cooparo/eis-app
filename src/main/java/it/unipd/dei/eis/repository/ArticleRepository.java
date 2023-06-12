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

public class ArticleRepository implements IRepository<IArticle> {

    private final ArrayList<IArticle> storage;
    private FileFormat fileFormat;
    private final static String BASE_PATH = "src/main/resources/";

    public ArticleRepository() {
        this(FileFormat.JSON);
    }

    public ArticleRepository(FileFormat fileFormat) {
        this.fileFormat = fileFormat;
        storage = ArticleStorage.getInstance().getArticleList();
    }

    public FileFormat getFileFormat() { return fileFormat; }
    public void setFileFormat(FileFormat format) { this.fileFormat = format; }

    public void add(IArticle... articles) {
        storage.addAll(Arrays.asList(articles));
    }

    public <NewspaperArticle> void add(ArrayList<NewspaperArticle> articles, Function<NewspaperArticle, IArticle> converter) {
        for (NewspaperArticle article : articles) {
            add(new Article(converter.apply(article)));
        }
    }

    @Override
    public ArrayList<IArticle> getAll() {
        return storage;
    }

    @Override
    public void update(IArticle oldArticle, IArticle newArticle) {
        if(storage.contains(oldArticle)) {
            int oldArticleIndex = storage.indexOf(oldArticle);
            storage.set(oldArticleIndex, newArticle);
        } else {
            throw new IllegalArgumentException("Article not found");
        }
    }

    @Override
    public void remove(String id) {
        storage.removeIf(article -> article.getId().equals(id));
    }

    @Override
    public int size() { return storage.size(); }

    @Override
    public ArrayList<IArticle> FindBySpecification(ISpecification spec) {
        ArrayList<IArticle> articleArrayList = new ArrayList<>();

        for(IArticle article : storage) {
            if(spec.exist(article)) articleArrayList.add(article);
        }

        return articleArrayList;
    }

    // PERSISTENCE
    private String getFilePath() {
        return BASE_PATH + "storage." + fileFormat.toString().toLowerCase();
    }
    public void saveToDisk() throws IOException {
        String serializedStorage = Marshalling.serialize(fileFormat, storage);
        IO.writeFile(getFilePath(), serializedStorage);
    }
    public void loadFromDisk() throws IOException {
        String serializedStorage = IO.readFile(getFilePath());
        ArrayList<Article> container = Marshalling.deserialize(fileFormat, serializedStorage, new Marshalling.TypeReference<ArrayList<Article>>(){});
        add(container.toArray(new IArticle[0]));
    }
}
