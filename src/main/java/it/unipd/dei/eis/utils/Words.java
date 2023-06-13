package it.unipd.dei.eis.utils;

public class Words {

    /**
     * Convert a string to camel case.
     * @param input The string to be converted.
     * @return The converted string.
     */
    public static String toCamelCase(String input) {
        String[] words = input.split(" ");
        StringBuilder output = new StringBuilder(words[0].toLowerCase());
        for (int i = 1; i < words.length; i++) {
            String word = words[i];
            String capitalized = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
            output.append(capitalized);
        }
        return output.toString();
    }
}
