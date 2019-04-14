import management.ModuleManagement;
import util.*;

import java.util.ArrayList;
import java.util.Arrays;

public class April {

    public static void main(String[] args) throws Exception {

        var list = new ArrayList<Integer>();

        for (int i = 0; i < 100; i++) {
            list.add(RandomUtil.random(i));
        }

        System.out.println(list);

        var l = Clearer.init(list)
                .removeRedundancies()
                .removeIf(val -> val < 10)
                .removeIf(val -> val >= 20)
                .toArray();

        System.out.println(Arrays.asList(l));
    }

    private static void registerNativeModules() {
        ModuleManagement.boot();
    }
}
