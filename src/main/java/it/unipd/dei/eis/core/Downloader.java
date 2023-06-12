package it.unipd.dei.eis.core;

import it.unipd.dei.eis.exceptions.MalformedPackageException;
import it.unipd.dei.eis.interfaces.IArticle;
import it.unipd.dei.eis.newspapers.TheGuardian.TGArticleAdapter;
import it.unipd.dei.eis.newspapers.TheGuardian.TGClient;
import it.unipd.dei.eis.newspapers.TheGuardian.models.TGArticle;
import it.unipd.dei.eis.newspapers.TheGuardian.models.TGResponseWrapper;
import it.unipd.dei.eis.repository.ArticleRepository;
import it.unipd.dei.eis.utils.FileFormat;
import it.unipd.dei.eis.utils.IO;
import it.unipd.dei.eis.utils.Marshalling;

import java.io.IOException;
import java.lang.reflect.*;
import java.util.ArrayList;

public class Downloader {
    private final ArticleRepository repository;

//    private static final String BASE_PACKAGE = "it.unipd.dei.eis.newspapers.";
    public Downloader(ArticleRepository repository) {
        this.repository = repository;
    }

    public void download(String newspaper, String query) {
        download(newspaper, query, false);
    }
    public void simulateDownload(String newspaper, String path) {
        download(newspaper, path, true);
    }

    private void download(String newspaper, String arg, boolean simulate) {

        final String shortNewspaper = extractUpperCaseLetters(newspaper);
        final String BASE_PACKAGE = "it.unipd.dei.eis.newspapers." + newspaper + "." + shortNewspaper;

        try {
            // Accessing the newspaper client
            final Class<?> newspaperClient = Class.forName(BASE_PACKAGE + "Client");

            // Accessing 'importArticleArrayListFromFile' method if simulate == true, else 'getArticleArrayList' method
            final Method getArticles = getMethod(newspaperClient, simulate);

            // Accessing NewspaperArticle type by parsing 'getArticles' method return type
            final Type baseType = getNewspaperArticleType(getArticles);

            // Preparing arguments for the invocation of the method
            Object[] arguments;
            if (simulate) arguments = new Object[] {arg};
            else arguments = new Object[] {arg, 100};

            // Creating an instance of the Newspaper Client
            Object clientInstance = newspaperClient.getDeclaredConstructor().newInstance();

            // Invoking the method
            ArrayList<?> articles = (ArrayList<?>) getArticles.invoke(clientInstance, arguments);

            // Accessing the Newspaper Adapter in order to store the articles in the repository
            Class<?> newspaperAdapter = Class.forName(BASE_PACKAGE + "ArticleAdapter");

            // Checking if the Newspaper Adapter implements IArticle
            if (!(IArticle.class.isAssignableFrom(newspaperAdapter))) {
                throw new MalformedPackageException(newspaper, "The adapter class must implement interfaces.IArticle");
            }
            Constructor<?> newspaperAdapterConstructor = newspaperAdapter.getDeclaredConstructor((Class<?>) baseType);

            for (Object article : articles) {
                repository.add((IArticle) newspaperAdapterConstructor.newInstance(article));
            }

            repository.saveToDisk();

        } catch (IOException e) {
            throw new RuntimeException("Could not save the articles in the hard disk.", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getCause());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not find the client or the article adapter for the newspaper " +
                    "you have requested. Please add them or verify if they follow the syntax explained in the manual.");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Functionality not supported. Verify that the methods signatures in the client " +
                    "follow the instructions provided in the manual.");
        } catch (InstantiationException e) {
            throw new RuntimeException("Could not instantiate the client or the adapter. " +
                    "Please verify that their constructors follow the instructions provided in the manual.");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Could not access to the constructors or the methods of the client or the adapter. " +
                    "Verify that the constructors or the methods are public, where required (read the manual).");
        }

    }
    private Method getMethod(Class<?> newspaperClient, boolean isSimulation) throws NoSuchMethodException {
        if (isSimulation)
            return newspaperClient.getMethod("importArticleArrayListFromFile", String.class);
        return newspaperClient.getMethod("getArticleArrayList", String.class, int.class);
    }

    private Type getNewspaperArticleType(Method method) {

        // Checking if the return type of the method is valid (must be ArrayList<NewspaperArticle>)
        Type returnType = method.getGenericReturnType();
        if (!(returnType instanceof ParameterizedType) || !(ArrayList.class.equals(((ParameterizedType) returnType).getRawType()))) {
            throw new MalformedPackageException(method.getDeclaringClass().getName(),
                    "Return type of method 'getArticleArrayList' must be java.util.ArrayList");
        }

        // Retrieving NewspaperArticle real class
        return ((ParameterizedType) returnType).getActualTypeArguments()[0];
    }

    private String extractUpperCaseLetters(String text) {
        StringBuilder uppercaseLetters = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c >= 'A' && c <= 'Z')
                uppercaseLetters.append(c);
        }
        return uppercaseLetters.toString();
    }


    // OLD METHODS, CLASSIC USAGE
    public void theGuardianDownloader() {
        final String API_KEY = System.getenv("THE_GUARDIAN_API_KEY");
        TGClient client = new TGClient(API_KEY);

        ArrayList<TGArticle> articles = client.getArticleArrayList("nuclear power", 1000);

        repository.add(articles, TGArticleAdapter::new);

    }

    public void theGuardianReader() throws IOException {
        String text = IO.readFile("./src/main/resources/the_guardian_response_1.json");
        TGResponseWrapper root = Marshalling.deserialize(FileFormat.JSON, text, TGResponseWrapper.class);

        ArrayList<TGArticle> articles = root.getResponse().getResults();
        repository.add(articles, TGArticleAdapter::new);
    }


}
