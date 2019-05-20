package model.persistence.builder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link InsertSQLBuilder}
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
public class InsertSQLBuilder extends AbstractBuilder implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String table;

    private final List<String> columns = new ArrayList<>();

    private final List<String> values = new ArrayList<>();

    public InsertSQLBuilder(String table) {
        this.table = table;
    }

    public InsertSQLBuilder set(String column, String value) {
        columns.add(column);
        values.add(value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sql = new StringBuilder("insert into ").append(table).append(" (");
        appendList(sql, columns, "", ", ");
        sql.append(") values (");
        appendList(sql, values, "", ", ");
        sql.append(");");
        return sql.toString();
    }
}
