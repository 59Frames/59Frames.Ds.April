package module.knowledge.crawler;

/**
 * {@link Match}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class Match {
    private final String match;

    public Match(String match) {
        this.match = match;
    }

    @Override
    public String toString() {
        return match;
    }
}
