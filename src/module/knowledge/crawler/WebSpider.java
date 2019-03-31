package module.knowledge.crawler;

import concurrent.Promise;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * {@link WebSpider}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class WebSpider {

    private static final int MAX_PAGES_TO_SEARCH_THROUGH = 1000;
    private LinkedList<String> pagesToVisit = new LinkedList<>();
    private HashSet<String> visitedPages = new HashSet<>();

    public Promise<Match[]> crawl(String url, String searchWord) {
        return new Promise<>(() -> {
            final var matches = new ArrayList<Match>();

            while (this.visitedPages.size() < MAX_PAGES_TO_SEARCH_THROUGH) {
                String currentUrl;
                final var leg = new WebSpiderLeg();

                if (this.pagesToVisit.isEmpty()) {
                    currentUrl = url;
                    this.pagesToVisit.add(url);
                } else {
                    currentUrl = this.nextUrl();
                }

                leg.crawl(currentUrl);

                if (leg.searchForWord(searchWord)) {
                    final var sentences = leg.getSentencesWithMatchingWord(searchWord);
                    matches.addAll(sentences.stream().map(sentence -> new Match(sentence, searchWord, currentUrl)).collect(Collectors.toList()));
                    break;
                }

                this.pagesToVisit.addAll(leg.getLinks());
            }

            var arr = new Match[matches.size()];
            return matches.toArray(arr);
        });
    }

    public Promise<Match[]> search(String keyword) {
        return crawl(String.format("https://www.google.com/search?q=%s", keyword), keyword);
    }

    private String nextUrl() {
        String nextUrl;

        do {
            nextUrl = this.pagesToVisit.removeFirst();
        } while (this.visitedPages.contains(nextUrl));

        this.visitedPages.add(nextUrl);
        return nextUrl;
    }
}
