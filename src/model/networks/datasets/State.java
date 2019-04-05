package model.networks.datasets;

/**
 * {@link State}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class State {
    private final Transition[] transitions;

    public State(Transition[] transitions) {
        this.transitions = transitions;
    }

    public int getTransitionLength() {
        return this.transitions.length;
    }

    public Transition get(int index) {
        return this.transitions[index];
    }
}
