import model.persistence.EntityManager;
import model.persistence.table.Blueprint;
import model.tables.Person;
import util.StringUtil;

import java.util.ArrayList;

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

        EntityManager.createTable(Person.class);

        ArrayList<Person> people = new ArrayList<>();

        people.add(new Person("Frank", "Seifert"));
        people.add(new Person("Katharina", "Seifert"));
        people.add(new Person("Maximilian", "Seifert"));
        people.add(new Person("Daniel", "Seifert"));
        people.add(new Person("Oliver", "Seifert"));

        for (Person p : people) {
            EntityManager.insertOrUpdate(p);
        }

        people = EntityManager.find(Person.class, p -> StringUtil.like(p.getFirstname(), "%an%"));

        for (Person p : people) {
            System.out.println(p);
        }
    }
}
