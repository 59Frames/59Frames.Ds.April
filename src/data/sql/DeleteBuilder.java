package data.sql;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DeleteBuilder extends AbstractSQLBuilder implements Serializable {

    private static final long serialVersionUID = 1;

    private final String table;

    private final List<String> wheres = new ArrayList<String>();

    public DeleteBuilder(String table) {
        this.table = table;
    }

    @Override
    public String toString() {
        StringBuilder sql = new StringBuilder("delete from ").append(table);
        appendList(sql, wheres, " where ", " and ");
        return sql.toString();
    }

    public DeleteBuilder where(String expr) {
        wheres.add(expr);
        return this;
    }

}
