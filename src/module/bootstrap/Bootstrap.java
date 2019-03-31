package module.bootstrap;

import _59frames.ds.lando.CommandListener;
import _59frames.ds.lando.model.Command;
import _59frames.ds.lando.model.Constraint;
import model.annotation.StaticClass;
import module.Module;
import module.sensorium.physical.Arc;
import util.CommandUtil;

/**
 * {@link Bootstrap}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */

@StaticClass
public class Bootstrap extends Module {
    /**
     * Constructs a new instance of type Module
     */
    public Bootstrap() {
        super("Bootstrap");
    }

    private void validateClasses() {

    }

    private void loadConfiguration() {
        Configuration.load();
    }

    private void registerCommandListener() {
        final boolean hasNamedArguments = Configuration.bootstrap().getBoolean("COMMAND_HAS_NAMED_ARGUMENTS");
        final boolean hasDefaultHelpCommand = Configuration.bootstrap().getBoolean("COMMAND_HAS_HELP_COMMAND");
        final boolean hasDefaultExitCommand = Configuration.bootstrap().getBoolean("COMMAND_HAS_EXIT_COMMAND");
        final boolean startsWithBuild = Configuration.bootstrap().getBoolean("COMMAND_STARTS_WITH_BUILD");

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
    }

    @Override
    protected void bootUp() {
        validateClasses();
        loadConfiguration();

        registerCommandListener();
        registerDefaultCommands();
    }
}
