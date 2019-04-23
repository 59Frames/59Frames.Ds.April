import model.interfaceable.Bootable;
import module.bootstrap.Bootstrap;

import java.lang.reflect.Method;

import static util.Toolbox.*;

public class April {

    public static void main(String[] args) throws Exception {
        Bootable bootable = cast(new Bootstrap(), Bootable.class);

        bootable.boot();
    }
}
