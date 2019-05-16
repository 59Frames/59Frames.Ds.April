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

    public Person(JSONObject object) {
        super(object);
        this.firstname = object.getString("firstname");
        this.lastname = object.getString("lastname");
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    @Override
    public String toString() {
        return String.format("%s %s with Id: %d | Last Update: %s | Initialized: %s", this.firstname, this.lastname, this.id, this.lastUpdate, this.initialDate);
    }
}
