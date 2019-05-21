package model.persistence.builder.sql;

import model.persistence.builder.AbstractSQLBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link SelectSQLBuilder}
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class SelectSQLBuilder extends AbstractSQLBuilder implements Cloneable, Serializable {

    private static final long serialVersionUID = 1L;

    private boolean distinct;

    private final List<Object> columns = new ArrayList<>();

    private final List<String> tables = new ArrayList<>();

    private final List<String> joins = new ArrayList<>();

    private final List<String> leftJoins = new ArrayList<>();

    private final List<String> wheres = new ArrayList<>();

    private final List<String> groupBys = new ArrayList<>();

    private final List<String> havings = new ArrayList<>();

    private final List<SelectSQLBuilder> unions = new ArrayList<>();

    private final List<String> orderBys = new ArrayList<>();

    private int limit = 0;

    private int offset = 0;

    private boolean forUpdate;

    private boolean noWait;

    public SelectSQLBuilder() {

    }

    public SelectSQLBuilder(String table) {
        tables.add(table);
    }

    /**
     * Alias for {@link #where(String)}.
     */
    public SelectSQLBuilder and(String expr) {
        return where(expr);
    }

    public SelectSQLBuilder column(String name) {
        columns.add(name);
        return this;
    }

    public SelectSQLBuilder column(String name, boolean groupBy) {
        columns.add(name);
        if (groupBy) {
            groupBys.add(name);
        }
        return this;
    }

    public SelectSQLBuilder limit(int limit, int offset) {
        this.limit = limit;
        this.offset = offset;
        return this;
    }

    public SelectSQLBuilder limit(int limit) {
        return limit(limit, 0);
    }

    @Override
    public SelectSQLBuilder clone() {
        try {
            return (SelectSQLBuilder) super.clone();
        } catch (CloneNotSupportedException e) {
            return new SelectSQLBuilder();
        }
    }

    public SelectSQLBuilder distinct() {
        this.distinct = true;
        return this;
    }

    public SelectSQLBuilder forUpdate() {
        forUpdate = true;
        return this;
    }

    public SelectSQLBuilder from(String table) {
        tables.add(table);
        return this;
    }

    public List<SelectSQLBuilder> getUnions() {
        return unions;
    }

    public SelectSQLBuilder groupBy(String expr) {
        groupBys.add(expr);
        return this;
    }

    public SelectSQLBuilder having(String expr) {
        havings.add(expr);
        return this;
    }

    public SelectSQLBuilder join(String join) {
        joins.add(join);
        return this;
    }

    public SelectSQLBuilder leftJoin(String join) {
        leftJoins.add(join);
        return this;
    }

    public SelectSQLBuilder noWait() {
        if (!forUpdate) {
            throw new RuntimeException("noWait without forUpdate cannot be called");
        }
        noWait = true;
        return this;
    }

    public SelectSQLBuilder orderBy(String name) {
        orderBys.add(name);
        return this;
    }

    /**
     * Adds an ORDER BY item with a direction indicator.
     *
     * @param name      Name of the column by which to sort.
     * @param ascending If true, specifies the direction "asc", otherwise, specifies
     *                  the direction "desc".
     */
    public SelectSQLBuilder orderBy(String name, boolean ascending) {
        if (ascending) {
            orderBys.add(name + " asc");
        } else {
            orderBys.add(name + " desc");
        }
        return this;
    }

    @Override
    public String toString() {

        StringBuilder sql = new StringBuilder("select ");

        if (distinct) {
            sql.append("distinct ");
        }

        if (columns.size() == 0) {
            sql.append("*");
        } else {
            appendList(sql, columns, "", ", ");
        }

        appendList(sql, tables, " from ", ", ");
        appendList(sql, joins, " join ", " join ");
        appendList(sql, leftJoins, " left join ", " left join ");
        appendList(sql, wheres, " where ", " and ");
        appendList(sql, groupBys, " group by ", ", ");
        appendList(sql, havings, " having ", " and ");
        appendList(sql, unions, " union ", " union ");
        appendList(sql, orderBys, " order by ", ", ");

        if (forUpdate) {
            sql.append(" for update");
            if (noWait) {
                sql.append(" nowait");
            }
        }

        if (limit > 0)
            sql.append(" limit ").append(limit);
        if (offset > 0)
            sql.append(", ").append(offset);

        return sql.toString();
    }

    /**
     * Adds a "union" select builder. The generated SQL will union this query
     * with the result of the main query. The provided builder must have the
     * same columns as the parent select builder and must not use "order by" or
     * "for update".
     */
    public SelectSQLBuilder union(SelectSQLBuilder unionBuilder) {
        unions.add(unionBuilder);
        return this;
    }

    public SelectSQLBuilder where(String expr) {
        wheres.add(expr);
        return this;
    }
}
