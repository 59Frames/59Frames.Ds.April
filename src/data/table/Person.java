package data.table;

import data.annotation.Column;
import data.annotation.PrimaryKey;
import data.annotation.Table;
import data.annotation.Type;

/**
 * {@link Person}
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
@Table(name = "People")
public class Person {

    @PrimaryKey
    private int id = 0;


}
