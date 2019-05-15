import data.Database;
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

        final String tableFormat = "| %-4s | %-15s | %-15s |%n";

        Database database = Database.getInstance();

        database.runRawQueryAsync("SELECT * FROM april.people").then(arr -> {

            System.out.format("+------+-----------------+-----------------+%n");
            System.out.printf(tableFormat, "ID", "Firstname", "Lastname");
            System.out.format("+------+-----------------+-----------------+%n");

            arr.forEach(obj -> {
                JSONObject object = (JSONObject) obj;
                System.out.printf(tableFormat, object.getInt("id"), object.getString("firstname"), object.getString("lastname"));
            });
            System.out.format("+------+-----------------+-----------------+%n");
        }).catchException(Debugger::exception);
    }
}
