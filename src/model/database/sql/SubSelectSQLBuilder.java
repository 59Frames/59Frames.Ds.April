package model.database.sql;

/**
 * SelectSQLBuilder that can be used as a sub-select in a column expression or FROM clause.
 *
 * @author <a href="mailto:john@krasnay.ca">John Krasnay</a>
 */
public class SubSelectSQLBuilder extends SelectSQLBuilder {

    private String alias;

    public SubSelectSQLBuilder(String alias) {
        this.alias = alias;
    }

    protected SubSelectSQLBuilder(SubSelectSQLBuilder other) {
        super(other);
        this.alias = other.alias;
    }

    @Override
    public SubSelectSQLBuilder clone() {
        return new SubSelectSQLBuilder(this);
    }

    @Override
    public String toString() {
        return "(" +
                super.toString() +
                ") as " +
                alias;
    }
}
