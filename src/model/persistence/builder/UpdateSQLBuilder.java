package model.persistence.builder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class UpdateSQLBuilder extends AbstractBuilder implements Serializable {

    private static final long serialVersionUID = 1L;

    private String table;

    private List<String> sets = new ArrayList<>();

    private List<String> wheres = new ArrayList<>();

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
