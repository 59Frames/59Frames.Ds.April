package model.persistence.builder;

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
public class DeleteSQLBuilder extends AbstractBuilder implements Serializable {

    private static final long serialVersionUID = 1;

    private final String table;

    private final List<String> wheres = new ArrayList<>();

    public DeleteSQLBuilder(String table) {
        this.table = table;
    }

    @Override
    public String toString() {
        StringBuilder sql = new StringBuilder("delete from ").append(table);
        appendList(sql, wheres, " where ", " and ");
        return sql.toString();
    }

    public DeleteSQLBuilder where(String expr) {
        wheres.add(expr);
        return this;
    }

}
