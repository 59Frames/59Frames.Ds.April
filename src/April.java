import data.EntityContext;
import model.database.Book;
import model.database.Person;

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

        EntityContext.createTable(Person.class);

        Person p = EntityContext.insertOrUpdate(new Person("Jon", "Doe"));

        p.setFirstname("John");

        EntityContext.insertOrUpdate(p);

        System.out.println(p);
    }
}
