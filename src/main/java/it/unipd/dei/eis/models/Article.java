package it.unipd.dei.eis.models;

import it.unipd.dei.eis.interfaces.IArticle;

import java.time.Instant;

public class Article {

    String id;
    private String title;
    private String body;
    private Instant publicationTime;

    public Article(String id, String title, String body, Instant publicationTime) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.publicationTime = publicationTime;
    }

    public Article(IArticle article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.body = article.getBody();
        this.publicationTime = article.getPublicationTime();
    }

    public Article() {}

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setPublicationTime(Instant publicationTime) { this.publicationTime = publicationTime; }
    public Instant getPublicationTime() { return this.publicationTime; }

}
