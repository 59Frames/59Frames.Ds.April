package model.persistence.builder.sql;

import model.persistence.builder.AbstractSQLBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link DeleteSQLBuilder}
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
public class DeleteSQLBuilder extends AbstractSQLBuilder implements Serializable {

    private static final long serialVersionUID = 1;

    private final String table;

    private final List<String> wheres = new ArrayList<>();
    private final List<Object> values = new ArrayList<>();

    public DeleteSQLBuilder(String table) {
        this.table = table;
    }

    public DeleteSQLBuilder where(String where, String operator, Object value) {
        wheres.add(String.format("%s %s ?", where, operator));
        values.add(value);
        return this;
    }

    public List<Object> getValues() {
        return values;
    }

    @Override
    public String toString() {
        StringBuilder sql = new StringBuilder("delete from ").append(table);
        appendList(sql, wheres, " where ", " and ");
        sql.append(";");
        return sql.toString();
    }
}
