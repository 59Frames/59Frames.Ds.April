package data.sql;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InsertBuilder extends AbstractSQLBuilder implements Serializable {

    private static final long serialVersionUID = 1;

    private final String table;

    private final List<String> columns = new ArrayList<String>();

    private final List<String> values = new ArrayList<String>();

    public InsertBuilder(String table) {
        this.table = table;
    }

    public InsertBuilder set(String column, String value) {
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
