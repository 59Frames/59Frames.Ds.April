package model.persistence.table;

import model.annotation.*;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Timestamp;

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
    protected Date initialDate;

    @Column
    protected Date lastUpdate;

    protected DatabaseObject() {
        this.initialDate = new Date(System.currentTimeMillis());
        this.lastUpdate = new Date(System.currentTimeMillis());
    }

    public DatabaseObject(JSONObject object) {
        this.id = object.getInt("id");
        this.initialDate = new Date(((Timestamp) object.get("initialDate")).getTime());
        this.lastUpdate = new Date(((Timestamp) object.get("lastUpdate")).getTime());
    }

    public int getId() {
        return id;
    }

    public Date getInitialDate() {
        return initialDate;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setInitialDate(Date initialDate) {
        this.initialDate = initialDate;
    }

    public void setLastUpdate(Date lastUpdate) {
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
