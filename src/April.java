import model.interfaceable.Bootable;
import module.bootstrap.Bootstrap;
import module.speech.language.SpellCorrector;
import util.CollectionUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;

import static util.Toolbox.*;

public class April {

    public static void main(String[] args) throws Exception {
        new Bootstrap().boot();
    }
}
