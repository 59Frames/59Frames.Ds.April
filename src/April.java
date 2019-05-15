import data.Database;
import data.annotation.Table;
import data.table.Person;
import org.json.JSONObject;
import util.Debugger;

import java.lang.annotation.Annotation;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

public class April {
    public static void main(String[] args) throws Exception {
        // TODO: 28/04/2019 bootstrap
        // Bootstrap.boot();

        // TODO: 28/04/2019 sensorium
        // TODO: 28/04/2019 speech
        // TODO: 28/04/2019 volition
        // TODO: 28/04/2019 knowledge
        // TODO: 28/04/2019 emotion
        // TODO: 28/04/2019 motorium

        Class<Person> personClass = Person.class;
        Annotation a = personClass.getAnnotation(Table.class);
        System.out.println(a);

        Field[] fields = personClass.getDeclaredFields();

        for (Field f : fields) {
            for (Annotation av : f.getAnnotations()) {
                System.out.println(av);
            }
            System.out.println(f.getName());
            System.out.println(f.getType());
        }
    }
}
