package it.unipd.dei.eis.repository;

import it.unipd.dei.eis.interfaces.IRepository;
import it.unipd.dei.eis.models.Article;
import it.unipd.dei.eis.interfaces.ISpecification;
import it.unipd.dei.eis.utils.Format;
import it.unipd.dei.eis.utils.IO;
import it.unipd.dei.eis.utils.Marshalling;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;

public class ArticleRepository implements IRepository<Article> {

    private final ArrayList<Article> storage;
    private final Format fileFormat;

    public ArticleRepository() {
        this(Format.JSON);
    }
    public ArticleRepository(Format fileFormat) {
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
            //TODO: throw an Exception
        }
    }

    @Override
    public void remove(String id) {
        for(Article article : storage) {
            if(article.getId().equals(id)) {
                storage.remove(article);
            }
        }
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
    private static class StorageContainer {
        private final ArrayList<Article> storage;
        StorageContainer(ArrayList<Article> storage) { this.storage = storage; }
        public ArrayList<Article> getStorage() { return storage; }
    }
    private String getFilePath() {
        return "./src/main/resources/storage." + fileFormat.toString().toLowerCase();
    }
    public void saveToDisk() throws IOException {
        String serializedStorage = Marshalling.serialize(fileFormat, new StorageContainer(storage));
        IO.writeFile(getFilePath(), serializedStorage);
    }
    public void loadFromDisk() throws IOException {
        String serializedStorage = IO.readFile(getFilePath());
        StorageContainer container = Marshalling.deserialize(fileFormat, serializedStorage, StorageContainer.class);
        add(container.getStorage().toArray(new Article[0]));
    }
}