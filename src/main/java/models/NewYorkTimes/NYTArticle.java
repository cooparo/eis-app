package models.NewYorkTimes;

import java.util.Date;

public class NYTArticle {
    private String identifier;
    private String url;
    private String title;
    private String body;
    private Date date;
    private String sourceSet;
    private String source;

    public NYTArticle(String identifier, String url, String title, String body, Date date, String sourceSet, String source) {
        this.identifier = identifier;
        this.url = url;
        this.title = title;
        this.body = body;
        this.date = date;
        this.sourceSet = sourceSet;
        this.source = source;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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
