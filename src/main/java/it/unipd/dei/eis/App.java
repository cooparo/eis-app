package it.unipd.dei.eis;

import io.github.cdimascio.dotenv.Dotenv;
import it.unipd.dei.eis.core.Downloader;
import it.unipd.dei.eis.core.Ranker;
import it.unipd.dei.eis.repository.ArticleRepository;
import it.unipd.dei.eis.utils.FileFormat;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class App {
    private static final ArticleRepository repository = new ArticleRepository();

    public static void main(String[] args) {

        // Dotenv configuration
        Dotenv dotenv = Dotenv
                .configure()
                .directory("./src/main/resources")
                .filename("env")
                .load();
        dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));

        // CLI, EXAMPLES & IMPLEMENTATION
        // eis-app -dr -n TheGuardian -q pasta
        // eis-app --download --rank --newspaper TheGuardian --query "pasta"
        // eis-app -d -f CSV -p "./src/main/resources/the_guardian_response_1.json"

        Options options = new Options();
        options.addOption(new Option("d", "download", false,
                "Downloads the articles or simulates a download by reading a file."));
        options.addOption(new Option("r", "rank", false, "Ranks the articles."));

        options.addOption(new Option("n", "newspaper", true,
                "The Newspaper you want to download from, it must be equal to the name of the package."));
        options.addOption(new Option("q", "query", true, "The query."));
        options.addOption(new Option("p", "path", true, "The path to the file."));
        options.addOption(new Option("f", "fileFormat", true, "File format for storage, " +
                "the available formats are: " + // JSON, XML, CSV [for example]
                Arrays.stream(FileFormat.values()).map((Enum::name)).collect(Collectors.joining(", "))));

        options.addOption(new Option("h", "help", false,
                "Prints a summary of all supported commands."));

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);

            if (cmd.hasOption("h")) {
                printHelp(options);
                return;
            }

            if (!(cmd.hasOption("d")) && !(cmd.hasOption("r")))
                throw new ParseException("Almost one of the options 'd' and 'r' must be present.");

            if (cmd.hasOption("f")) {
                repository.setFileFormat(
                        FileFormat.valueOf(cmd.getOptionValue("f").toUpperCase()));
            }

            if (cmd.hasOption("d")) {

                if (cmd.hasOption("n")) {
                    if (cmd.hasOption("q") && cmd.hasOption("p"))
                        throw new ParseException("You can't select both q and p options.");
                    if (!(cmd.hasOption("q") || cmd.hasOption("p")))
                        throw new ParseException("You must specify either a query or a path to a file.");
                } else {
                    if (cmd.hasOption("q") || cmd.hasOption("p"))
                        throw new ParseException("You must specify a newspaper.");
                }

                try { repository.loadFromDisk(); System.out.println("Articles loaded.\n"); }
                catch (NoSuchFileException e) { System.out.println("No previously downloaded articles found.\n"); }

                Downloader downloader = new Downloader(repository);

                if (cmd.hasOption("q")) {
                    downloader.download(cmd.getOptionValue("n"), cmd.getOptionValue("q"));
                } else if (cmd.hasOption("p")) {
                    downloader.simulateDownload(cmd.getOptionValue("n"), cmd.getOptionValue("p"));
                } else {
                    downloader.download("TheGuardian", "pizza");
                }
            }
            if (cmd.hasOption("r")) {
                repository.loadFromDisk();

                Ranker ranker = new Ranker(repository);
                ranker.rank();
                System.out.println(ranker.getWordFrequencyReport(10));
                System.out.println("For more words, look into the file ranked.txt");
                ranker.saveOnTxt("ranked.txt");
            }

        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("No enum constant")) {
                System.err.println("ERROR - File format not valid.");
                printHelp(options);
            } else throw e;
        } catch (ParseException e) {
            System.err.println("ERROR - parsing command line:");
            System.err.println(e.getMessage());
            printHelp(options);
        } catch (NoSuchFileException e) {
            System.err.println("ERROR - Could not find file " + e.getFile()); // TODO: more descriptive error?
        } catch (IOException e) {
            System.err.println("ERROR - " + e.getMessage());
        }
    }

    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("eis-app -{d,r,dr} [options]", options);

        System.out.println();
        System.out.println("Examples:");
        System.out.println("eis-app -dr -n TheGuardian -q pasta -f XML");
        System.out.println("eis-app --download --rank --newspaper TheGuardian --query pasta");
        System.out.println("eis-app --download -n NewYorkTimes -p \"./src/main/resources/nytimes_articles_v2.csv\"");
    }
}

