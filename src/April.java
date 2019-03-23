import _59frames.ds.lando.CommandListener;
import _59frames.ds.lando.model.Command;
import module.bootstrap.Bootstrap;

public class April {
    public static void main(String[] args) {
        var listener = CommandListener.builder()
                .hasNamedArguments(false)
                .startWithBuild()
                .build();

        listener.add(new Command("start", arguments -> Bootstrap.load()));
    }
}
