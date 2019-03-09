package module.knowledge.crawler;

/**
 * {@link Match}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class Match {
    private final String sentence;
    private final String keyword;
    private final String url;

    public Match(String sentence, String keyword, String url) {
        this.sentence = sentence;
        this.keyword = keyword;
        this.url = url;
    }

    @Override
    public String toString() {
        return sentence;
    }
}
