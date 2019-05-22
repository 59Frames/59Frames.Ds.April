package model.persistence.builder.sql;

import model.persistence.FieldColumn;
import model.persistence.builder.AbstractSQLBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * {@link CreateSQLBuilder}
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
public class CreateSQLBuilder extends AbstractSQLBuilder {

    private final String tableName;

    private final HashSet<FieldColumn> columns = new HashSet<>();

    public CreateSQLBuilder(@NotNull final String tableName) {
        this.tableName = tableName;
    }

    public void add(@NotNull final FieldColumn column) {
        this.columns.add(column);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("CREATE TABLE");
        builder.append(" ").append(this.tableName).append(" (");

        switch (dialect) {
            case SQLITE:
                createSQLITETable(builder);
                break;
            case H2:
                createH2Table(builder);
                break;
            case MYSQL:
                createMYSQLTable(builder);
                break;
        }

        return builder.toString();
    }

    private void createMYSQLTable(StringBuilder builder) {
        ArrayList<FieldColumn> uniqueColumns = new ArrayList<>();
        final FieldColumn[] primaryKeyColumn = new FieldColumn[1];
        primaryKeyColumn[0] = null;
        final boolean[] first = new boolean[1];
        first[0] = true;
        columns.forEach(fieldColumn -> {
            if (!first[0])
                builder.append(", ");

            builder.append(fieldColumn.stringFor(dialect));

            if (fieldColumn.isPrimaryKey())
                primaryKeyColumn[0] = fieldColumn;

            if (!fieldColumn.isPrimaryKey() && fieldColumn.isUnique())
                uniqueColumns.add(fieldColumn);

            first[0] = false;
        });

        if (primaryKeyColumn[0] != null) {
            builder.append(", ")
                    .append("CONSTRAINT")
                    .append(" ")
                    .append(this.tableName)
                    .append("_pk")
                    .append(" ")
                    .append("primary key")
                    .append(" ")
                    .append("(")
                    .append(primaryKeyColumn[0].getName())
                    .append(")");
        }

        builder.append(");");

        if (!uniqueColumns.isEmpty()) {
            for (FieldColumn column : uniqueColumns) {
                builder.append("\n").append("\n")
                        .append("CREATE UNIQUE INDEX")
                        .append(" ")
                        .append(this.tableName)
                        .append("_")
                        .append(column.getName())
                        .append("_uindex")
                        .append(" ")
                        .append("ON")
                        .append(" ")
                        .append(this.tableName)
                        .append(" ")
                        .append("(")
                        .append(column.getName());
            }

            builder.append(");");
        }
    }

    private void createH2Table(StringBuilder builder) {
        final FieldColumn[] primaryKeyColumn = new FieldColumn[1];
        primaryKeyColumn[0] = null;
        final boolean[] first = new boolean[1];
        first[0] = true;
        columns.forEach(fieldColumn -> {
            if (!first[0])
                builder.append(", ");

            builder.append(fieldColumn.stringFor(dialect));

            if (fieldColumn.isPrimaryKey())
                primaryKeyColumn[0] = fieldColumn;

            first[0] = false;
        });

        if (primaryKeyColumn[0] != null) {
            builder.append(", ")
                    .append("PRIMARY KEY")
                    .append("(")
                    .append(primaryKeyColumn[0].getName())
                    .append(")");
        }

        builder.append(");");
    }

    private void createSQLITETable(StringBuilder builder) {
        final boolean[] first = new boolean[1];
        first[0] = true;
        columns.forEach(column -> {
            if (!first[0])
                builder.append(", ");

            builder.append(column.stringFor(dialect));

            first[0] = false;
        });

        builder.append(");");
    }
}
