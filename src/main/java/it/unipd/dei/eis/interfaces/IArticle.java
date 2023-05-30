package it.unipd.dei.eis.interfaces;
import java.time.Instant;
public interface IArticle {
    String getId();
    String getTitle();
    String getBody();
    Instant getPublicationTime();
}
