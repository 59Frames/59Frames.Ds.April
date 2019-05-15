import data.Database;
import data.table.Blueprint;
import model.database.Book;
import model.database.Person;
import org.json.JSONObject;
import util.Debugger;

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

        String sql = Blueprint.of(Book.class).toString();

        System.out.println(sql);

        Database.getInstance().runRawUpdateAsync(sql)
                .then(System.out::println)
                .catchException(Debugger::exception);
    }
}
