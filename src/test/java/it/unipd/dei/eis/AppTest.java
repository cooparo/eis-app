package it.unipd.dei.eis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.PriorityQueue;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    public static String readFile(String filePath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    @Test
    void rankTester() {

        String input = readFile("src/test/resources/lorem-ipsum.txt");

        PriorityQueue<Map.Entry<String, Integer>> result = App.rank(input);

        Map.Entry<String, Integer> entry = result.poll();
        Assertions.assertEquals("egestas", entry.getKey());
        Assertions.assertEquals(14, entry.getValue());

        entry = result.poll();
        Assertions.assertEquals("ut", entry.getKey());
        Assertions.assertEquals(14, entry.getValue());

        entry = result.poll();
        Assertions.assertEquals("eget", entry.getKey());
        Assertions.assertEquals(12, entry.getValue());

        entry = result.poll();
        Assertions.assertEquals("in", entry.getKey());
        Assertions.assertEquals(12, entry.getValue());

        entry = result.poll();
        Assertions.assertEquals("neque", entry.getKey());
        Assertions.assertEquals(12, entry.getValue());

    }
}