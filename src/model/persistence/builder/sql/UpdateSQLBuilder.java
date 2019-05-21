package model.persistence.builder.sql;

import model.persistence.builder.AbstractSQLBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link UpdateSQLBuilder}
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
public class UpdateSQLBuilder extends AbstractSQLBuilder implements Serializable {

    private static final long serialVersionUID = 1L;

    private String table;

    private List<String> sets = new ArrayList<>();
    private List<Object> setValues = new ArrayList<>();

    private List<String> wheres = new ArrayList<>();
    private List<Object> whereValues = new ArrayList<>();

    public UpdateSQLBuilder(String table) {
        this.table = table;
    }

    public UpdateSQLBuilder set(String column, Object value) {
        sets.add(String.format("%s = ?", column));
        setValues.add(value);
        return this;
    }

    public UpdateSQLBuilder where(String column, String operator, Object value) {
        wheres.add(String.format("%s %s ?", column, operator));
        whereValues.add(value);
        return this;
    }

    public List<Object> getSetValues() {
        return setValues;
    }

    public List<Object> getWhereValues() {
        return whereValues;
    }

    @Override
    public String toString() {
        StringBuilder sql = new StringBuilder("update ").append(table);
        appendList(sql, sets, " set ", ", ");
        appendList(sql, wheres, " where ", " and ");
        sql.append(";");
        return sql.toString();
    }
}
