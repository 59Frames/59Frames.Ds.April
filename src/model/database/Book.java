package model.database;

import data.annotation.*;

/**
 * {@link Book}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
@Table(name = "books")
public class Book {
    @Column
    @PrimaryKey
    @AutoIncrement
    private int id;

    @Column
    @Unique
    @Required
    @WithLength(length = 255)
    private String ISBN;

    @Column
    @Required
    private String title;
}
