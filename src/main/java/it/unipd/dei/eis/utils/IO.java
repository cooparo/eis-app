package it.unipd.dei.eis.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class IO {
    public static String readFile(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
    }
    public static void writeFile(String path, String data) throws IOException {
        Files.write(Paths.get(path),
                data.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }
    public static String getFileExtension(String path) {
        int dotIndex = path.lastIndexOf(".");
        if (dotIndex < 0) return "";
        return path.substring(dotIndex + 1);
    }
}
