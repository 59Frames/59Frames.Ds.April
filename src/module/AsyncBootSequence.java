package module;

import model.interfaceable.Bootable;
import util.ThreadService;

/**
 * AsyncBootSequence
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class AsyncBootSequence extends Context implements Bootable {
    private final Thread bootThread;

    /**
     * Constructs a new instance of type AsyncBootSequence
     */
    protected AsyncBootSequence() {
        this.bootThread = new Thread(() -> {
            this.updateState(State.BOOTING);
            this.bootUp();
            this.updateState(State.ONLINE);
            this.onHasBeenBootedUp();
        });
    }

    /**
     * creates a new Thread in which the module
     * gets booted up and calls the abstract
     * bootUp method. If successful (No exception was thrown),
     * the state is set to online and the onHasBeenBootedUp
     * method gets called
     */
    public final void boot() {
        if (this.state == State.OFFLINE)
            ThreadService.execute(this.bootThread);
    }

    /**
     * Gets called when the module has been booted up
     */
    protected void onHasBeenBootedUp() {
    }

    /**
     * subclass defines what should happen in a boot up
     */
    protected abstract void bootUp();
}
