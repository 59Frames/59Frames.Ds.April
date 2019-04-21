package module.bootstrap;

import _59frames.ds.lando.CommandListener;
import _59frames.ds.lando.model.Command;
import _59frames.ds.lando.model.Constraint;
import environment.Environment;
import module.Module;
import module.sensorium.physical.Arc;
import module.sensorium.sense.hearing.buffer.CircularByteBufferHolder;
import util.CommandUtil;
import util.Debugger;

/**
 * {@link Bootstrap}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */

public final class Bootstrap extends Module {
    /**
     * Constructs a new instance of type Module
     */
    public Bootstrap() {
        super("Bootstrap");
    }

    private void loadEnvironment() {
        Environment.load();
    }

    private void registerCommandListener() {
        final boolean hasNamedArguments = Environment.getBoolean("lando.named");
        final boolean hasDefaultHelpCommand = Environment.getBoolean("lando.help");
        final boolean hasDefaultExitCommand = Environment.getBoolean("lando.exit");
        final boolean startsWithBuild = Environment.getBoolean("lando.swb");

        CommandUtil.registerListener(
                CommandListener.builder()
                        .startWithBuild(startsWithBuild)
                        .hasNamedArguments(hasNamedArguments)
                        .hasDefaultHelpCommand(hasDefaultHelpCommand)
                        .hasDefaultExitCommand(hasDefaultExitCommand)
                        .input(System.in)
                        .errorOutput(System.err)
                        .build()
        );

        Debugger.info("Command Listener Registered");
    }

    private void registerDefaultCommands() {
        final var getCommand = new Command("get", arguments -> {
            final var type = arguments.get("type").getValue().toLowerCase();
            final var object = arguments.get("of").getValue().toLowerCase();

            var out = "";
            if (type.equals("information")
                    || type.equals("info")
                    || type.equals("i")) {
                switch (object.toLowerCase()) {
                    case "cpu":
                        out = Arc.getCPU().getName();
                        break;
                    case "version":
                        out = Arc.getVersion().getVersion();
                        break;
                    case "network":
                        out = Arc.getNetworkParams().getHostName();
                        break;
                    case "sensors":
                        out = String.valueOf(Arc.getSensors().getCpuTemperature());
                        break;
                    default:
                        out = "";
                }
            }

            System.out.println(out);

        }, new Constraint("type", true), new Constraint("of", true));

        CommandUtil.add(getCommand);

        Debugger.info("Default Commands Registered");
    }

    @Override
    protected void bootUp() {
        loadEnvironment();
        loadArc();

        registerCommandListener();
        registerBuffers();

        registerDefaultCommands();
    }

    private void loadArc() {
        try {
            Class.forName("module.sensorium.physical.Arc");

            Debugger.info("Arc Registered");
        } catch (ClassNotFoundException e) {
            Debugger.exception(e);
        }
    }

    private void registerBuffers() {
        CircularByteBufferHolder.registerCircularBuffer(Environment.getInteger("module.sensorium.sense.hearing.buffer.capacity"));

        Debugger.info("Buffers Registered");
    }
}
