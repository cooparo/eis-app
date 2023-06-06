package it.unipd.dei.eis;

import it.unipd.dei.eis.core.Downloader;
import it.unipd.dei.eis.core.Ranker;
import it.unipd.dei.eis.repository.ArticleRepository;
import it.unipd.dei.eis.utils.FileFormat;
import org.apache.commons.cli.*;

import java.io.IOException;

public class App {
    private static final ArticleRepository repository = new ArticleRepository(FileFormat.CSV);

    public static void main(String[] args) throws IOException {
        // TODO: configure dotenv

        // EXAMPLES
        // eis-app.exe --download --rank --search TheGuardian pasta
        // eis-app.exe -dr -f CSV

        Options options = new Options();
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(new Option("d", "download", false, "Downloads the articles"));
        optionGroup.addOption(new Option("r", "rank", false, "Ranks the articles"));
        optionGroup.setRequired(true);
        // FIXME OptionGroup does not permit both options 'd' and 'r' to be specified, but only one a time.
        // FIXME writing -dr will throw: "Error parsing command line arguments: The option 'r' was specified but an option from this group has already been selected: 'd'"
        // FIXME found manual solution with ChatGPT, without OptionGroup, but have no time to implement it right now

        options.addOptionGroup(optionGroup);
        options.addOption(new Option("f", "fileFormat", true, "File format for storage"));
        // FIXME I can not modify fileFormat of ArticleRepository since it is final and static
        options.addOption(new Option("q", "query", true, "[Newspaper]-[Query]"));

        HelpFormatter formatter = new HelpFormatter();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.err.println("ERROR - parsing command line:");
            System.err.println(e.getMessage());
            formatter.printHelp("eis-app -{d,r,dr} [options]", options); // TODO write it better
            return;
        }
        if (cmd.hasOption("d")) {
            download();

            System.out.println(repository.getAll().get(0).getTitle());
            System.out.println(repository.getAll().get(0).getBody());
        }
        if (cmd.hasOption("r")) {
            rank();
        }

    }
    public static void download() throws IOException {
        Downloader downloader = new Downloader(repository);
        downloader.theGuardianReader();

        repository.saveToDisk();
    }
    public static void rank() throws IOException {
        repository.loadFromDisk();

        Ranker ranker = new Ranker(repository);
        ranker.saveOnTxt("ranked.txt");
    }
}

