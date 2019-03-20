import util.CollectionUtil;
import util.RandomUtil;

import java.util.ArrayList;

public class April {
    public static void main(String[] args) {
        ArrayList<Integer> newList = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            newList.add(RandomUtil.random(0, 50));
        }

        var testList = CollectionUtil.clearAndRemoveIf(newList, val -> val.equals(0));

        System.out.println(testList.size());
    }
}
