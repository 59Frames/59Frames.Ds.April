package model.persistence.builder.sql;

import model.persistence.builder.AbstractSQLBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@link InsertSQLBuilder}
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
public class InsertSQLBuilder extends AbstractSQLBuilder implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String table;

    private final List<String> columns = new ArrayList<>();

    private final List<Object> values = new ArrayList<>();

    public InsertSQLBuilder(String table) {
        this.table = table;
    }

    public InsertSQLBuilder set(String column, Object value) {
        columns.add(column);
        values.add(value);
        return this;
    }

    public List<Object> getValues() {
        return values;
    }

    @Override
    public String toString() {
        StringBuilder sql = new StringBuilder("insert into ").append(table).append(" (");
        appendList(sql, columns, "", ", ");
        sql.append(") values (");
        appendList(sql, Collections.nCopies(values.size(), "?"), "", ", ");
        sql.append(");");
        return sql.toString();
    }
}
