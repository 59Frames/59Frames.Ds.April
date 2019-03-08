package module.knowledge.crawler;

import util.Debugger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@link Spider}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class Spider {

    private static final int MAX_PAGES_TO_SEARCH_THROUGH = 1000;
    public LinkedList<String> pagesToVisit = new LinkedList<>();
    public HashSet<String> visitedPages = new HashSet<>();

    public Match[] search(String url, String searchWord) {
        final var matches = new ArrayList<Match>();

        while (this.visitedPages.size() < MAX_PAGES_TO_SEARCH_THROUGH) {
            String currentUrl;
            final var leg = new SpiderLeg();

            if (this.pagesToVisit.isEmpty()) {
                currentUrl = url;
                this.pagesToVisit.add(url);
            } else {
                currentUrl = this.nextUrl();
            }

            leg.crawl(currentUrl);

            if (leg.searchForWord(searchWord)) {
                final var sentences = leg.getSentencesWithMatchingWord(searchWord);
                matches.addAll(sentences.stream().map(Match::new).collect(Collectors.toList()));
                break;
            }
            this.pagesToVisit.addAll(leg.getLinks());
        }

        var arr = new Match[matches.size()];
        return matches.toArray(arr);
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
