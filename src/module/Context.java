package module;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Context
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
public class Context {
    protected State state;

    @Contract(pure = true)
    public Context() {
        this.state = State.OFFLINE;
    }

    /**
     * @return - whether state is online or not
     */
    @Contract(pure = true)
    public final boolean isOnline() {
        return this.state == State.ONLINE;
    }

    /**
     * @return - whether state is paused or not
     */
    @Contract(pure = true)
    public final boolean isPaused() {
        return this.state == State.PAUSED;
    }

    /**
     * @return - whether state is offline or not
     */
    @Contract(pure = true)
    public final boolean isOffline() {
        return this.state == State.OFFLINE;
    }

    /**
     * @return - whether state is resuming or not
     */
    @Contract(pure = true)
    public final boolean isResuming() {
        return this.state == State.RESUMING;
    }

    /**
     * @return - whether state is booting or not
     */
    @Contract(pure = true)
    public final boolean isBooting() {
        return this.state == State.RESUMING;
    }

    /**
     * @return - the current state
     */
    @Contract(pure = true)
    public final State getState() {
        return this.state;
    }

    /**
     * Updates the current state of this module and
     * logs an info
     *
     * @param newState - the new state
     */
    protected void updateState(@NotNull final State newState) {
        this.state = newState;
        this.onStateHasChanged(this.state);
    }

    /**
     * Gets called when the state has changed
     * can and should be overridden in child classes
     *
     * @param newState - the new state
     */
    protected void onStateHasChanged(@NotNull final State newState) {
    }
}
