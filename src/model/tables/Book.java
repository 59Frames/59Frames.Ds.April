package model.tables;

import model.annotation.*;
import model.database.table.DatabaseObject;
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

    public Book(String title, String ISBN) {
        this.title = title;
        this.ISBN = ISBN;
    }

    public Book(JSONObject object) {
        super(object);
        this.ISBN = object.getString("ISBN");
        this.title = object.getString("title");
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void fillJSON(JSONObject object) {
        object.put("ISBN", this.ISBN);
        object.put("title", this.title);
    }

    @Override
    public String toString() {
        return title;
    }
}
