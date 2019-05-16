package model.database;

import data.annotation.*;
import data.table.DatabaseObject;
import org.json.JSONObject;

/**
 * {@link Person}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
@Table(name = "people")
public class Person extends DatabaseObject {

    @Column
    @Required
    @WithLength(length = 255)
    private String firstname;

    @Column
    @Required
    @WithLength(length = 255)
    private String lastname;

    @Column
    @Required
    private boolean male;

    public Person(String first, String last, boolean male) {
        super();
        this.firstname = first;
        this.lastname = last;
        this.male = male;
    }

    public Person(JSONObject object) {
        super(object);
        this.firstname = object.getString("firstname");
        this.lastname = object.getString("lastname");
        this.male = object.getInt("male") == 1;
    }

    @Override
    public void fillJSON(JSONObject object) {
        object.put("firstname", this.firstname);
        object.put("lastname", this.lastname);
        object.put("male", this.male);
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public boolean isMale() {
        return male;
    }

    @Override
    public String toString() {
        return String.format("%s %s with Id: %d | Last Update: %s | Initialized: %s", this.firstname, this.lastname, this.id, this.lastUpdate, this.initialDate);
    }
}
