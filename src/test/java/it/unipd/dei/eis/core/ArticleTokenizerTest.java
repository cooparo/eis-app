package it.unipd.dei.eis.core;

import it.unipd.dei.eis.interfaces.IArticle;
import it.unipd.dei.eis.repository.Article;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ArticleTokenizerTest {

    IArticle article;
    IArticle punctuationArticle;

    @BeforeEach
    void setUp() {

        article = new Article(
          "",
          "This is a dummy title",
          "Malesuada fames ac turpis egestas maecenas pharetra. Mattis enim ut tellus elementum sagittis vitae et leo duis. Cursus euismod quis viverra nibh cras pulvinar. Nascetur ridiculus mus mauris vitae ultricies leo integer malesuada nunc. Aliquam faucibus purus in massa tempor nec feugiat nisl. Mauris rhoncus aenean vel elit scelerisque mauris pellentesque pulvinar. Eget egestas purus viverra accumsan in nisl nisi scelerisque. Urna cursus eget nunc scelerisque viverra mauris in aliquam sem. Magna sit amet purus gravida quis blandit turpis cursus in. Sem integer vitae justo eget magna. Vitae tempus quam pellentesque nec nam aliquam sem. Leo duis ut diam quam nulla porttitor massa id neque. Lorem dolor sed viverra ipsum nunc aliquet. Nec feugiat nisl pretium fusce id velit. Sed egestas egestas fringilla phasellus faucibus.",
          null
        );

        punctuationArticle = new Article(
                "",
                "",
                ":_;;_:;:_;:_;:_;_!!£%/(  .@`#@]`@# @#` ùò@# `³23 ³`²#@ $)/!%&)!/&%/$!%$!%!)(£ /(&$/%&£)(/%$  £/($",
                null
        );
    }

    @Test
    void tokenizeEasyArticle() {

        Set<String> tokens = ArticleTokenizer.tokenize(article);

        assertNotNull(tokens);
        assertEquals(80, tokens.size());
        assertTrue(tokens.contains("malesuada"));
        assertTrue(tokens.contains("dummy"));

    }

    @Test
    void tokenizePunctuationArticle() {

        Set<String> tokens = ArticleTokenizer.tokenize(punctuationArticle);

        assertNotNull(tokens);
        assertEquals(0, tokens.size());
    }
}