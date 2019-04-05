package model.networks.datasets;

/**
 * {@link Transition}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class Transition {

    private final int nextStateId;
    private final int token;

    public Transition(int nextStateId, int token) {
        this.nextStateId = nextStateId;
        this.token = token;
    }

    public int getNextStateId() {
        return this.nextStateId;
    }

    public int getToken() {
        return this.token;
    }
}
