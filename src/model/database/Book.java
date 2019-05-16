package model.database;

import data.annotation.*;
import data.table.DatabaseObject;
import org.json.JSONObject;

/**
 * {@link Book}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
@Table(name = "books")
public class Book extends DatabaseObject {
    @Column
    @Unique
    @Required
    @WithLength(length = 255)
    private String ISBN;

    @Column
    @Required
    private String title;

    public Book(JSONObject object) {
        super(object);
        this.ISBN = object.getString("ISBN");
        this.title = object.getString("title");
    }

    @Override
    public void fillJSON(JSONObject object) {
        object.put("ISBN", this.ISBN);
        object.put("title", this.title);
    }
}
