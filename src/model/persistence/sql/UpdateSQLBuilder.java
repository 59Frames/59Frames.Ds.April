package model.persistence.sql;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder for creating SQL update statements.
 *
 * @author John Krasnay <john@krasnay.ca>
 */
public class UpdateSQLBuilder extends AbstractSQLBuilder implements Serializable {

    private static final long serialVersionUID = 1;

    private String table;

    private List<String> sets = new ArrayList<String>();

    private List<String> wheres = new ArrayList<String>();

    public UpdateSQLBuilder(String table) {
        this.table = table;
    }

    public UpdateSQLBuilder set(String expr) {
        sets.add(expr);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sql = new StringBuilder("update ").append(table);
        appendList(sql, sets, " set ", ", ");
        appendList(sql, wheres, " where ", " and ");
        return sql.toString();
    }

    public UpdateSQLBuilder where(String expr) {
        wheres.add(expr);
        return this;
    }

}
