package data.table;

import data.annotation.AutoIncrement;
import data.annotation.Column;
import data.annotation.PrimaryKey;
import org.json.JSONObject;
import util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.Date;

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
        this.initialDate = new Date(Calendar.getInstance().getTime().getTime());
        this.lastUpdate = new Date(Calendar.getInstance().getTime().getTime());
    }

    public DatabaseObject(JSONObject object) {
        this.id = object.getInt("id");
        this.initialDate = new Date(DateUtil.parse(String.valueOf(object.get("initialDate")), "yyyy-mm-dd hh:mm:ss").getTime());
        this.lastUpdate = new Date(DateUtil.parse(String.valueOf(object.get("lastUpdate")), "yyyy-mm-dd hh:mm:ss").getTime());
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
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        object.put("id", this.id);
        object.put("initialDate", this.initialDate);
        object.put("lastUpdate", this.lastUpdate);
        fillJSON(object);
        return object;
    }

    protected abstract void fillJSON(JSONObject object);
}
