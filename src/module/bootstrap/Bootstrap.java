package module.bootstrap;

/**
 * {@link Bootstrap}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class Bootstrap {
    public static void load() {
        var name = Configuration.april().get("NAME");
        System.out.println(name);
    }
}
