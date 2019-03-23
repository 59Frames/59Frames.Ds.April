import _59frames.ds.lando.CommandListener;
import _59frames.ds.lando.model.Command;
import _59frames.ds.lando.model.Constraint;
import module.bootstrap.Bootstrap;
import module.sensorium.Arc;

public class April {
    public static void main(String[] args) {
        var listener = CommandListener.builder()
                .hasNamedArguments(false)
                .startWithBuild()
                .build();

        listener.add(new Command("start", arguments -> Bootstrap.load()));
        listener.add(new Command("get", arguments -> {
            final var type = arguments.get("type").getValue();
            final var object = arguments.get("of").getValue();

            var out = "";
            if (type.equals("information")) {
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

        }, new Constraint("type", true), new Constraint("of", true)));
    }
}
