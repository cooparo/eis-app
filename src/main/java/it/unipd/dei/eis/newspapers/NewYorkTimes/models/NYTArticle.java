package it.unipd.dei.eis.newspapers.NewYorkTimes.models;

import java.time.Instant;

public class NYTArticle {
    private String identifier;
    private String url;
    private String title;
    private String body;
    private Instant date;
    private String sourceSet;
    private String source;

    public NYTArticle(String identifier, String url, String title, String body, Instant date, String sourceSet, String source) {
        this.identifier = identifier;
        this.url = url;
        this.title = title;
        this.body = body;
        this.date = date;
        this.sourceSet = sourceSet;
        this.source = source;
    }

    public NYTArticle() {

    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

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

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getSourceSet() {
        return sourceSet;
    }

    public void setSourceSet(String sourceSet) {
        this.sourceSet = sourceSet;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
