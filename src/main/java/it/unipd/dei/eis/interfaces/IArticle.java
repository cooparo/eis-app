package it.unipd.dei.eis.interfaces;

import java.time.Instant;

/**
 * An interface that is implemented by the adapters of the newspapers and the Article class.
 */
public interface IArticle {
    String getId();
    String getTitle();
    String getBody();
    Instant getPublicationTime();
}
