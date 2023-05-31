package it.unipd.dei.eis.models;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import it.unipd.dei.eis.interfaces.IArticle;

@JsonPropertyOrder({"id", "title", "body"})
public class Article {

    String id;
    private String title;
    private String body;

    public Article(String id, String title, String body) {
        this.id = id;
        this.title = title;
        this.body = body;
    }

    public Article(IArticle article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.body = article.getBody();
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

}
