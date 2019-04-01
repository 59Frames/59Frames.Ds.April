package module.sensorium;

import _59frames.ds.lando.model.Command;
import module.Module;
import module.sensorium.physical.Arc;
import module.sensorium.sense.hearing.Recorder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * {@link Sensorium}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public final class Sensorium extends Module {
    /**
     * Constructs a new instance of type Module
     */
    public Sensorium() {
        super("Sensorium");
    }

    @Override
    protected void bootUp() {

    }

    @Override
    protected void onRegisteringCommands(@NotNull final List<Command> commands) {
        commands.addAll(Arc.getInteractableCommands());
    }
}
