package it.unipd.dei.eis.core;

import it.unipd.dei.eis.exceptions.MalformedPackageException;
import it.unipd.dei.eis.interfaces.IArticle;
import it.unipd.dei.eis.repository.ArticleRepository;

import java.io.IOException;
import java.lang.reflect.*;
import java.util.ArrayList;

/**
 * One of the two core functionalities of this software.
 */
public class Downloader {
    private final ArticleRepository repository;
    private static final int NUMBER_ARTICLES_DOWNLOADED = 100;

    public Downloader(ArticleRepository repository) {
        this.repository = repository;
    }

    /**
     * Downloads 100 articles of the specified newspaper with the specified query,
     * adds them in the repository (whose object you passed when creating a Downloader object)
     * and save them to disk.
     * <p>
     * The specified newspaper must have a <code>newspapers.[NewspaperName]</code> package containing
     * a <code>[NN]Client</code> class and a <code>[NN]</code>ArticleAdapter.
     * Please read the manual for further explanation.
     *
     * @param newspaper The newspaper you want to download the articles from
     * @param query     The query you want to send to the server
     */
    public void download(String newspaper, String query) {
        download(newspaper, query, false);
    }

    /**
     * Reads the articles of the specified newspaper from the specified file,
     * adds them in the repository (whose object you passed when creating a Downloader object)
     * and save them to disk.
     * <p>
     * The specified newspaper must have a <code>newspapers.[NewspaperName]</code> package containing
     * a <code>[NN]Client</code> class and a <code>[NN]</code>ArticleAdapter.
     * Please read the manual for further explanation.
     *
     * @param newspaper The newspaper you previously downloaded the articles with
     * @param path      The path to the file you want to read
     */
    public void simulateDownload(String newspaper, String path) {
        download(newspaper, path, true);
    }

    /**
     * Unique <i>private</i> method for downloading articles from a newspaper server or reading them from a file. <p>
     * Downloaded articles will be added in the repository too (whose object you passed when creating a Downloader object)
     * and they will be saved to disk.
     *
     * @param newspaper The newspaper you want the articles to be parsed with
     * @param arg       The <i>query</i> or the <i>path to the file</i>
     * @param simulate  Tf true, arg is considered a path to the file to be read. Otherwise, it is considered the query to be sent to the newspaper server.
     */
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
            if (simulate) arguments = new Object[]{arg};
            else arguments = new Object[]{arg, NUMBER_ARTICLES_DOWNLOADED};

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

    /**
     * Returns the right method to call based on the argument isSimulation.
     *
     * @param newspaperClient The newspaper client, organised as specified in the manual
     * @param isSimulation    Must be equal to the argument <i>simulate</i> in the calling method (download(String,String,boolean))
     * @return The right method
     * @throws NoSuchMethodException If the class is not organised as specified in the manual
     */
    private Method getMethod(Class<?> newspaperClient, boolean isSimulation) throws NoSuchMethodException {
        if (isSimulation)
            return newspaperClient.getMethod("importArticleArrayListFromFile", String.class);
        return newspaperClient.getMethod("getArticleArrayList", String.class, int.class);
    }

    /**
     * Returns the type parameter contained in the return type of the specified method.
     * For example, given the following method: <code>ArrayList&lt;Person&gt; method(String arg)</code>,
     * <code>Person</code> is returned.
     *
     * @param method the method you want to work with
     * @return the type parameter contained in the return type of the specified method
     */
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

    /**
     * Extracts only uppercase letter.
     * For example: <i>TheGuardian</i> would be turned into <i>TG</i>
     *
     * @param text The text you want to extract uppercase letters from
     * @return the extracted uppercase letters
     */
    private String extractUpperCaseLetters(String text) {
        StringBuilder uppercaseLetters = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c >= 'A' && c <= 'Z')
                uppercaseLetters.append(c);
        }
        return uppercaseLetters.toString();
    }
}
