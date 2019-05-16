import data.Database;
import data.table.Blueprint;
import model.concurrent.Promise;
import model.database.Book;
import model.database.Person;
import model.interfaceable.Processable;
import org.json.JSONObject;
import util.Debugger;
import util.Util;

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

        Database.getInstance().getAllAsync(Person.class).then(result -> {
            result.forEach(System.out::println);
        });

    }
}
