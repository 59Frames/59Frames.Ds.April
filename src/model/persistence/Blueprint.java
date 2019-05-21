package model.persistence;

import model.annotation.*;
import model.exception.MissingAnnotationException;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * {@link Blueprint}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class Blueprint {

    private final String tableName;
    private final ArrayList<BlueprintColumn> columns;

    private Blueprint(String tableName, ArrayList<BlueprintColumn> columns) {
        this.tableName = tableName;
        this.columns = columns;
    }

    public static synchronized Blueprint of(Class<? extends DatabaseObject> c) {
        Table tableAnnotation = c.getDeclaredAnnotation(Table.class);

        if (tableAnnotation == null)
            throw new MissingAnnotationException("Table Annotation not found");

        final ArrayList<BlueprintColumn> columns = new ArrayList<>();

        Field[] declaredSuperclassFields = c.getSuperclass().getDeclaredFields();
        Field[] declaredInheritedFields = c.getDeclaredFields();

        columns.addAll(getBlueprintColumns(declaredSuperclassFields));
        columns.addAll(getBlueprintColumns(declaredInheritedFields));

        return new Blueprint(tableAnnotation.name(), columns);
    }

    private static ArrayList<BlueprintColumn> getBlueprintColumns(Field[] fields) {
        final ArrayList<BlueprintColumn> columns = new ArrayList<>();

        for (Field f : fields) {
            if (f.getAnnotation(Column.class) == null)
                continue;

            String name = f.getName();
            Type fieldType = getFieldType(f);
            boolean isPrimaryKey = f.getAnnotation(PrimaryKey.class) != null;
            boolean isUnique = f.getAnnotation(Unique.class) != null;
            boolean isRequired = f.getAnnotation(Required.class) != null;
            boolean isAutoIncrement = f.getAnnotation(AutoIncrement.class) != null;
            boolean hasLength = f.getAnnotation(WithLength.class) != null;
            int length = -1;

            final String defaultValue = f.getAnnotation(Default.class) != null
                    ? f.getAnnotation(Default.class).value()
                    : "";

            if (isUnique) {
                if (!hasLength)
                    throw new MissingAnnotationException("Missing the @WithLength annotation");
            }

            if (hasLength) {
                fieldType = Type.VARCHAR;
                length = f.getAnnotation(WithLength.class).length();
            }

            BlueprintColumn column = BlueprintColumn.builderOf(name, fieldType)
                    .primaryKey(isPrimaryKey)
                    .autoIncrement(isAutoIncrement)
                    .defaultValue(defaultValue)
                    .required(isRequired)
                    .unique(isUnique)
                    .length(length)
                    .build();
            columns.add(column);
        }

        return columns;
    }

    private static Type getFieldType(@NotNull final Field f) {
        String fieldType = f.getType().getTypeName();

        switch (fieldType) {
            case "java.lang.String":
                return Type.TEXT;
            case "boolean":
                return Type.TINYINT;
            case "int":
                return Type.INT;
            case "short":
                return Type.MEDIUMINT;
            case "long":
                return Type.BIGINT;
            case "double":
                return Type.DOUBLE;
            case "float":
                return Type.FLOAT;
            case "char":
                return Type.CHAR;
            case "java.util.Date":
            case "java.sql.Date":
            case "java.sql.Timestamp":
                return Type.DATETIME;
            default: {
                if (f.getType().getSuperclass() != null) {
                    if (f.getType().getSuperclass().getSimpleName().equals("Enum"))
                        return Type.ENUM;
                }
            }
        }

        return Type.UNDEFINED;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("CREATE TABLE");
        builder.append(" ").append(this.tableName).append(" (");

        ArrayList<BlueprintColumn> uniqueColumns = new ArrayList<>();
        BlueprintColumn currentColumn = null;
        BlueprintColumn primaryKeyColumn = null;
        for (int i = 0; i < columns.size(); i++) {
            currentColumn = columns.get(i);
            builder.append(currentColumn.toString());

            if (currentColumn.isPrimaryKey())
                primaryKeyColumn = currentColumn;

            if (!currentColumn.isPrimaryKey() && currentColumn.isUnique())
                uniqueColumns.add(currentColumn);

            if (i != columns.size() - 1)
                builder.append(", ");
        }

        if (primaryKeyColumn != null) {
            builder.append(", ")
                    .append("CONSTRAINT")
                    .append(" ")
                    .append(this.tableName)
                    .append("_pk")
                    .append(" ")
                    .append("primary key")
                    .append(" ")
                    .append("(")
                    .append(primaryKeyColumn.getName())
                    .append(")");
        }

        builder.append(");");

        for (BlueprintColumn column : uniqueColumns) {
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
                    .append(column.getName())
                    .append(");");
        }

        return builder.toString();
    }
}
