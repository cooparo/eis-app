package it.unipd.dei.eis.repository;

import it.unipd.dei.eis.interfaces.IRepository;
import it.unipd.dei.eis.interfaces.ISpecification;
import it.unipd.dei.eis.utils.FileFormat;
import it.unipd.dei.eis.utils.IO;
import it.unipd.dei.eis.utils.Marshalling;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;

public class ArticleRepository implements IRepository<Article> {

    private final ArrayList<Article> storage;
    private final FileFormat fileFormat;
    private final static String BASE_PATH = "src/main/resources/";

    public ArticleRepository() {
        this(FileFormat.JSON);
    }

    public ArticleRepository(FileFormat fileFormat) {
        this.fileFormat = fileFormat;
        storage = ArticleStorage.getInstance().getArticleList();
    }

    @Override
    public void add(Article... articles) {
        storage.addAll(Arrays.asList(articles));
    }

    @Override
    public ArrayList<Article> getAll() {
        return storage;
    }

    @Override
    public void update(Article oldArticle, Article newArticle) {
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
    public ArrayList<Article> FindBySpecification(ISpecification spec) {
        ArrayList<Article> articleArrayList = new ArrayList<>();

        for(Article article : storage) {
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
        add(container.toArray(new Article[0]));
    }
}
