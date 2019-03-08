package module.knowledge.crawler;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * {@link SpiderLeg}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class SpiderLeg {

    private static final Pattern END_OF_SENTENCE_REGEX = Pattern.compile("[.?!]\\s+");
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";

    private List<String> links = new LinkedList<>();
    private Document htmlDocument;

    public void crawl(String url) {
        try {
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            Document htmlDocument = connection.get();
            this.htmlDocument = htmlDocument;

            System.out.println("Received web page at " + url);

            Elements linksOnPage = htmlDocument.select("a[href]");
            System.out.println("Found (" + linksOnPage.size() + ") links");
            for (Element link : linksOnPage) {
                this.links.add(link.absUrl("href"));
            }
        } catch (IOException ioe) {
            System.out.println("Error in out HTTP request " + ioe);
        }
    }

    public boolean searchForWord(String word) {
        return this.htmlDocument.body().text().toLowerCase().contains(word.toLowerCase());
    }

    public List<String> getSentencesWithMatchingWord(String word) {
        final List<String> foundSentences = new ArrayList<>();

        Elements bodyElements = this.htmlDocument.body().getAllElements();

        bodyElements.forEach(element -> {
            String el = element.text();
            Scanner scanner = new Scanner(el);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                if (line.contains(word)) {
                    foundSentences.add(getSentence(line, word));
                }
            }

        });

        return foundSentences;
    }

    private String getSentence(String text, @NotNull String word) {
        final String lowerCasedWord = word.toLowerCase().trim();
        return END_OF_SENTENCE_REGEX.splitAsStream(text)
                .filter(s -> s.toLowerCase().contains(lowerCasedWord))
                .findAny()
                .orElse(null);
    }

    public List<String> getLinks() {
        return this.links;
    }
}