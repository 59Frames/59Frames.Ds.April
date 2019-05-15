package model.database;

import data.annotation.*;
import org.json.JSONObject;

/**
 * {@link Person}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
@Table(name = "people")
public class Person {
    @Column
    @PrimaryKey
    @AutoIncrement
    private int id;

    @Column
    @Required
    @WithLength(length = 255)
    private String firstname;

    @Column
    @Required
    @WithLength(length = 255)
    private String lastname;

    public Person(int id, String firstname, String lastname) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public Person(JSONObject object) {
        this.id = object.getInt("id");
        this.firstname = object.getString("firstname");
        this.lastname = object.getString("lastname");
    }

    @Override
    public String toString() {
        return String.format("%s %s with Id: %d", this.firstname, this.lastname, this.id);
    }
}
