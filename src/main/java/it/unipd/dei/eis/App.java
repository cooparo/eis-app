package it.unipd.dei.eis;

import it.unipd.dei.eis.core.Downloader;
import it.unipd.dei.eis.core.Ranker;
import it.unipd.dei.eis.repository.ArticleRepository;
import it.unipd.dei.eis.utils.FileFormat;
import org.apache.commons.cli.*;

import java.io.IOException;

public class App {
    private static final ArticleRepository repository = new ArticleRepository();

    public static void main(String[] args) throws IOException {
        // TODO: configure dotenv

        // EXAMPLES
        // eis-app -dr -n TheGuardian -q pasta
        // eis-app --download --rank --newspaper TheGuardian --query pasta
        // eis-app -d -f CSV -p "./src/main/resources/the_guardian_response_1.json"

        Options options = new Options();
        options.addOption(new Option("d", "download", false,
                "Downloads the articles or simulates a download by reading a file."));
        options.addOption(new Option("r", "rank", false, "Ranks the articles."));

        options.addOption(new Option("n", "newspaper", true,
                "The Newspaper you want to download from, it must be equal to the name of the package"));
        options.addOption(new Option("q", "query", true, "The query"));
        options.addOption(new Option("p", "path", true, "The path to the file."));
        options.addOption(new Option("f", "fileFormat", true, "File format for storage"));

        HelpFormatter formatter = new HelpFormatter();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);

            if (!(cmd.hasOption("d")) && !(cmd.hasOption("r")))
                throw new ParseException("Almost one of the options 'd' and 'r' must be present.");

            if (cmd.hasOption("f")) {
                repository.setFileFormat(
                        FileFormat.valueOf(cmd.getOptionValue("f").toUpperCase()));
            }

            if (cmd.hasOption("d")) {

                if (cmd.hasOption("q") && cmd.hasOption("p"))
                    throw new ParseException("You can't select both q and p options.");

                // repository.loadFromDisk(); // TODO handle non-existent file

                Downloader downloader = new Downloader(repository);

                if (cmd.hasOption("q")) {
                    downloader.download(cmd.getOptionValue("n"), cmd.getOptionValue("q"));
                } else if (cmd.hasOption("p")) {
                    downloader.simulateDownload(cmd.getOptionValue("n"), cmd.getOptionValue("p"));
                } else {
                    downloader.download("TheGuardian", "pizza");
                }

                System.out.println(repository.getAll().get(0).getTitle());
                System.out.println(repository.getAll().get(0).getBody());
            }
            if (cmd.hasOption("r")) {
                repository.loadFromDisk();

                Ranker ranker = new Ranker(repository);
                ranker.saveOnTxt("ranked.txt");
            }

        } catch (ParseException | IllegalArgumentException e) { // TODO handle better
            System.err.println("ERROR - parsing command line:");
            System.err.println(e.getMessage());
            formatter.printHelp("eis-app -{d,r,dr} [options]", options); // TODO write it better
        }

    }
}

