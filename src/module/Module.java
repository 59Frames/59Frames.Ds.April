package module;

import util.Debugger;

/**
 * A Module is one small part of April.
 * Each Module has a single responsibility.
 * This class helps not losing the overview
 * and simplifies the state handling
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class Module extends AsyncBootSequence {

    private final String moduleName;

    /**
     * Constructs a new instance of type Module
     */
    public Module(String moduleName) {
        this.moduleName = moduleName;
    }

    /**
     * pauses the module and calls
     * class defined onPause method.
     */
    public final void pause() {
        if (this.state == State.PAUSED)
            return;

        this.onPause();
        this.updateState(State.PAUSED);
    }

    /**
     * disposes the module and calls
     * class defined onDispose method.
     */
    public final void dispose() {
        if (this.state == State.OFFLINE || this.state != State.ONLINE)
            return;

        this.onDispose();
        this.updateState(State.OFFLINE);
    }

    /**
     * Resumes the module if it has been paused
     */
    public final void resume() {
        if (this.state != State.PAUSED)
            return;

        this.updateState(State.RESUMING);
        this.onResume();
        this.updateState(State.ONLINE);
    }

    /**
     * @return - the moduleName
     */
    public String getModuleName() {
        return moduleName;
    }

    /**
     * gets called when the module gets disposed
     */
    protected void onDispose() {
    }

    /**
     * gets called when the module gets paused
     */
    protected void onPause() {
    }

    /**
     * gets called when the module gets paused
     */
    protected void onResume() {
    }

    @Override
    protected void onHasBeenBootedUp() {
        Debugger.info(String.format("Booted Up: %s \n", this.moduleName));
    }
}
