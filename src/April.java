import management.ModuleManagement;
import model.lang.BinarySearchTree;
import util.*;

import java.util.ArrayList;
import java.util.Arrays;

public class April {

    public static void main(String[] args) throws Exception {
        var bst = new BinarySearchTree<String>();

        var strings = new ArrayList<String>();

        for (int i = 0; i < 10; i++) {
            bst.insert(RandomUtil.randomString(RandomUtil.random(1, 10)));

            strings.add(String.valueOf(i));
        }

        System.out.println(strings);
        System.out.println(CollectionUtil.reverse(strings));

        System.out.println(StringUtil.timeString(System.currentTimeMillis()));
    }

    private static void registerNativeModules() {
        ModuleManagement.boot();
    }
}
