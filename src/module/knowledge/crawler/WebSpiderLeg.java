package module.knowledge.crawler;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import util.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * {@link WebSpiderLeg}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class WebSpiderLeg {

    private static final Pattern END_OF_SENTENCE_REGEX = Pattern.compile("[.?!]\\s+");
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";

    private List<String> links = new LinkedList<>();
    private Document htmlDocument;

    public void crawl(String url) {
        try {
            var connection = Jsoup.connect(url).userAgent(USER_AGENT);
            var htmlDocument = connection.get();
            this.htmlDocument = htmlDocument;

            var linksOnPage = htmlDocument.select("a[href]");
            for (var link : linksOnPage) {
                this.links.add(link.absUrl("href"));
            }
        } catch (IOException ioe) {
            System.out.println("Error in out HTTP request " + ioe);
        }
    }

    public boolean searchForWord(String word) {
        if (this.htmlDocument == null)
            return false;
        return this.htmlDocument.body().text().toLowerCase().contains(word.toLowerCase());
    }

    public List<String> getSentencesWithMatchingWord(String word) {
        final var foundSentences = new ArrayList<String>();

        var bodyElements = this.htmlDocument.body().getAllElements();

        bodyElements.forEach(element -> {
            var el = element.text();
            var scanner = new Scanner(el);

            while (scanner.hasNextLine()) {
                var line = StringUtil.clean(scanner.nextLine());

                if (line.toLowerCase().contains(word.toLowerCase())) {
                    foundSentences.add(getSentence(line, word));
                }
            }

        });

        return foundSentences;
    }

    private String getSentence(String text, @NotNull String word) {
        final var lowerCasedWord = word.toLowerCase().trim();
        return END_OF_SENTENCE_REGEX.splitAsStream(text)
                .filter(s -> s.toLowerCase().contains(lowerCasedWord))
                .findAny()
                .orElse(null);
    }

    public List<String> getLinks() {
        return this.links;
    }
}