import management.ModuleManagement;
import model.lang.BinarySearchTree;
import util.RandomUtil;
import util.StringUtil;

public class April {

    public static void main(String[] args) throws Exception {
        var bst = new BinarySearchTree<String>();

        for (int i = 0; i < 100000; i++) {
            bst.insert(RandomUtil.randomString(RandomUtil.random(1, 10)));
        }

        bst.traverse();
    }

    private static void registerNativeModules() {
        ModuleManagement.boot();
    }
}
