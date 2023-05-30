package it.unipd.dei.eis.utils;

import it.unipd.dei.eis.models.Article;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;
import java.util.PriorityQueue;

import static org.junit.jupiter.api.Assertions.*;

class RankerTest {

    Ranker ranker;
    ArrayList<Article> articleArrayList;

    @Test
    void rankTest() {
        Article article1 = new Article(
                "01",
                "Trump lawyer said to have been waved off searching office for secret records",
                "Donald Trump’s lawyer tasked with searching for classified documents at Mar-a-Lago after the " +
                        "justice department issued a subpoena told associates that he was waved off from searching the former president’s office, " +
                        "where the FBI later found the most sensitive materials anywhere on the property."
        );
        Article article2 = new Article(
                "02",
                "US supreme court end-of-term decisions could transform key areas of public life",
                "The US supreme court is gearing itself up for the final nail-biting month of its 2022-3 term in which " +
                        "it will deliver decisions that could transform critical areas of public life, from affirmative action in " +
                        "colleges to voting rights, LGBTQ+ equality and the future of Native American tribes."
        );
        Article article3 = new Article(
                "02",
                "Theranos founder Elizabeth Holmes expected to begin 11-year sentence",
                "The Theranos founder Elizabeth Holmes is poised to turn herself in for an 11-year prison sentence Tuesday, " +
                        "marking a final chapter in a years-long fraud saga that riveted Silicon Valley."
        );

        articleArrayList = new ArrayList<>();
        articleArrayList.add(article1);
        articleArrayList.add(article2);
        articleArrayList.add(article3);

        ranker = new Ranker();
        ranker.rank(articleArrayList);

        PriorityQueue<Map.Entry<String, Integer>> wordFrequencyMap = ranker.getWordFrequencyMap();
        while (!ranker.getWordFrequencyMap().isEmpty()) {
            Map.Entry<String, Integer> entry = wordFrequencyMap.poll();
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }
}