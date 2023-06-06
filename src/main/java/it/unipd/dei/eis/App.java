package it.unipd.dei.eis;

import it.unipd.dei.eis.core.Downloader;
import it.unipd.dei.eis.core.Ranker;
import it.unipd.dei.eis.repository.ArticleRepository;
import it.unipd.dei.eis.utils.FileFormat;

import java.io.IOException;

public class App {
    private static final ArticleRepository repository = new ArticleRepository(FileFormat.CSV);

    public static void main(String[] args) throws IOException {
        // EXAMPLES
        // eis-app.exe --download --rank --search TheGuardian pasta
        // eis-app.exe -dr -f CSV

        download();

        System.out.println(repository.getAll().get(0).getTitle());
        System.out.println(repository.getAll().get(0).getBody());
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

