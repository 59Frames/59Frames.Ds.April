package data.table;

import data.annotation.AutoIncrement;
import data.annotation.Column;
import data.annotation.PrimaryKey;
import org.json.JSONObject;
import util.DateUtil;
import util.Debugger;
import util.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * {@link DatabaseObject}
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
public class DatabaseObject {
    @Column
    @PrimaryKey
    @AutoIncrement
    protected int id;

    @Column
    protected Date initialDate;

    @Column
    protected Date lastUpdate;

    public DatabaseObject(JSONObject object) {
        this.id = object.getInt("id");
        try {
            this.initialDate = DateUtil.parse(String.valueOf(object.get("initialDate")), "yyyy-mm-dd hh:mm:ss");
            this.lastUpdate = DateUtil.parse(String.valueOf(object.get("lastUpdate")), "yyyy-mm-dd hh:mm:ss");
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
}
