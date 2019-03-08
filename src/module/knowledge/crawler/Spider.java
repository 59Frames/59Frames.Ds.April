package module.knowledge.crawler;

import util.Debugger;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

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
    public Pattern httpPattern = Pattern.compile("http[s]*://(\\w+\\.)*(\\w+)");

    public void search(String url, String searchWord) {
        while (this.visitedPages.size() < MAX_PAGES_TO_SEARCH_THROUGH) {
            String currentUrl;
            SpiderLeg leg = new SpiderLeg();

            if (this.pagesToVisit.isEmpty()) {
                currentUrl = url;
                this.pagesToVisit.add(url);
            } else {
                currentUrl = this.nextUrl();
            }

            leg.crawl(currentUrl);

            if (leg.searchForWord(searchWord)) {
                List<String> sentences = leg.getSentencesWithMatchingWord(searchWord);

                sentences.forEach(System.out::println);
                break;
            }
            this.pagesToVisit.addAll(leg.getLinks());
        }
        System.out.println("\n**Done** Visited " + this.visitedPages.size() + " web page(s)");
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
