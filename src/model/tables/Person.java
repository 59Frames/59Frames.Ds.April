package model.tables;

import model.annotation.Column;
import model.annotation.Required;
import model.annotation.Table;
import model.annotation.WithLength;
import model.persistence.DatabaseObject;
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

    public Person(String first, String last) {
        super();
        this.firstname = first;
        this.lastname = last;
    }

    public Person(JSONObject object) {
        super(object);
        this.firstname = object.getString("firstname");
        this.lastname = object.getString("lastname");
    }

    @Override
    public void fillJSON(JSONObject object) {
        object.put("firstname", this.firstname);
        object.put("lastname", this.lastname);
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

    @Override
    public String toString() {
        return String.format("%s %s with Id: %d | Last Update: %s | Initialized: %s", this.firstname, this.lastname, this.id, this.lastUpdate, this.initialDate);
    }
}
