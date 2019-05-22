package model.tables;

import model.annotation.Column;
import model.annotation.Required;
import model.annotation.Table;
import model.annotation.WithLength;
import model.persistence.DatabaseObject;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

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
    private String firstName;

    @Column
    @Required
    @WithLength(length = 255)
    private String lastName;

    @Column
    private Timestamp birthday;

    public Person(String first, String last, Timestamp birthday) {
        super();
        this.firstName = first;
        this.lastName = last;
        this.birthday = birthday;
    }

    public Person(JSONObject object) {
        super(object);
        this.firstName = object.getString("firstName");
        this.lastName = object.getString("lastName");
        this.birthday = (Timestamp) object.get("birthday");
    }

    @Override
    public void fillJSON(JSONObject object) {
        object.put("firstName", this.firstName);
        object.put("lastName", this.lastName);
        object.put("birthday", this.birthday);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return Period.between(LocalDate.ofInstant(birthday.toInstant(), ZoneId.systemDefault()), LocalDate.now()).getYears();
    }

    public Timestamp getBirthday() {
        return birthday;
    }

    @Override
    public String toString() {
        return String.format("Person[%d]: %s %s is %d years old. Initialized the %s and last updated %d days ago.", this.getId(), this.firstName, this.lastName, this.getAge(), this.initialDate, this.getDaysPastSinceLastUpdate());
    }
}
