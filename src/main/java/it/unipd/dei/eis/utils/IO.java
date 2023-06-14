package it.unipd.dei.eis.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class IO {

    /**
     * Reads the given file and returns its content as a string.
     * @param path the path of the file
     * @return the content of the file
     * @throws IOException if an I/O error occurs
     */
    public static String readFile(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
    }

    public static String readResourceFile(String path) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (InputStream inputStream = IO.class.getResourceAsStream(path);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    contentBuilder.append(line);
                    contentBuilder.append(System.lineSeparator());
                }
             }
        return contentBuilder.toString();
    }

    /**
     * Writes the given data to the given file, overwriting it if necessary.
     * @param path the path of the file
     * @param data the data to write
     * @throws IOException if an I/O error occurs
     */
    public static void writeFile(String path, String data) throws IOException {
        Files.write(Paths.get(path),
                data.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }

    /**
     * Returns the file extension of the given path.
     * @param path the path of the file
     * @return the file extension
     */
    public static String getFileExtension(String path) {
        int dotIndex = path.lastIndexOf(".");
        if (dotIndex < 0) return "";
        return path.substring(dotIndex + 1);
    }
}
