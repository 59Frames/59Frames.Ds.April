import model.persistence.DatabaseObject;
import model.persistence.EntityManager;
import model.tables.Person;
import util.CollectionUtil;
import util.DataAnalysis;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;

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

        EntityManager.fetchAllAsync(Person.class).then(people -> {
            people.sort(Comparator.comparingInt(DatabaseObject::getId));
            String leftAlignFormat = "| %-4s | %-20s | %-20s | %-4s |%n";

            System.out.printf(leftAlignFormat, "ID", "First Name", "Last Name", "Age");
            for (Person person : people) {
                System.out.format(leftAlignFormat, person.getId(), person.getFirstName(), person.getLastName(), person.getAge());
            }
        });
    }
}
