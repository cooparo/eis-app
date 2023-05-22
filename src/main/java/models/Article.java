package models;

public class Article {

    long id;
    private String title;
    private String body;

    public Article(long id, String title, String body) {
        this.id = id;
        this.title = title;
        this.body = body;
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
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

}
