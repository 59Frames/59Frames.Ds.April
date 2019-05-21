import model.persistence.EntityManager;
import model.persistence.builder.UpdateSQLBuilder;
import model.tables.Person;
import util.Kryptonite;

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


        EntityManager.findFirstAsync(Person.class, p -> p.getFirstname().equals("Daniel")).then(person -> {
            System.out.println(person.getId());
            System.out.println(person.getFirstname());
            System.out.println(person.getLastname());
            System.out.println(person.getInitialDate());
        });
    }
}
