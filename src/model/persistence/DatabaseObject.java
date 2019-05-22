package model.persistence;

import model.annotation.*;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

/**
 * {@link DatabaseObject}
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class DatabaseObject {
    @Column
    @PrimaryKey
    @AutoIncrement
    protected int id;

    @Column
    protected Timestamp initialDate;

    @Column
    protected Timestamp lastUpdate;

    protected DatabaseObject() {
        this.initialDate = new Timestamp(System.currentTimeMillis());
        this.lastUpdate = new Timestamp(System.currentTimeMillis());
    }

    public DatabaseObject(JSONObject object) {
        this.id = object.getInt("id");
        this.initialDate = (Timestamp) object.get("initialDate");
        this.lastUpdate = (Timestamp) object.get("lastUpdate");
    }

    public int getId() {
        return id;
    }

    public Timestamp getInitialDate() {
        return initialDate;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public int getDaysPastSinceInitialized() {
        return Period.between(LocalDate.ofInstant(initialDate.toInstant(), ZoneId.systemDefault()), LocalDate.now()).getDays();
    }

    public int getDaysPastSinceLastUpdate() {
        return Period.between(LocalDate.ofInstant(lastUpdate.toInstant(), ZoneId.systemDefault()), LocalDate.now()).getDays();
    }

    void update() {
        this.lastUpdate = new Timestamp(System.currentTimeMillis());
    }

    void setId(int id) {
        this.id = id;
    }

    void setInitialDate(Timestamp initialDate) {
        this.initialDate = initialDate;
    }

    void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public JSONObject toJSON() {
        JSONObject object = new JSONObject();
        object.put("id", this.id);
        object.put("initialDate", this.initialDate);
        object.put("lastUpdate", this.lastUpdate);
        fillJSON(object);
        return object;
    }

    protected abstract void fillJSON(JSONObject object);
}
