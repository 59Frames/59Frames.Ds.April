package model.persistence;

import model.annotation.*;
import model.exception.MissingAnnotationException;
import model.persistence.builder.sql.CreateSQLBuilder;
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
    private final ArrayList<FieldColumn> columns;

    private CreateSQLBuilder builder;

    private Blueprint(String tableName, ArrayList<FieldColumn> columns) {
        this.tableName = tableName;
        this.columns = columns;
        this.builder = new CreateSQLBuilder(tableName);
    }

    public static synchronized Blueprint of(Class<? extends DatabaseObject> c) {
        Table tableAnnotation = c.getDeclaredAnnotation(Table.class);

        if (tableAnnotation == null)
            throw new MissingAnnotationException("Table Annotation not found");

        final ArrayList<FieldColumn> columns = new ArrayList<>();

        Field[] declaredSuperclassFields = c.getSuperclass().getDeclaredFields();
        Field[] declaredInheritedFields = c.getDeclaredFields();

        columns.addAll(getBlueprintColumns(declaredSuperclassFields));
        columns.addAll(getBlueprintColumns(declaredInheritedFields));

        return new Blueprint(tableAnnotation.name(), columns);
    }

    public synchronized Blueprint with(CreateSQLBuilder builder) {
        this.builder = builder;
        return this;
    }

    private static ArrayList<FieldColumn> getBlueprintColumns(Field[] fields) {
        final ArrayList<FieldColumn> columns = new ArrayList<>();

        for (Field f : fields) {
            if (f.getAnnotation(model.annotation.Column.class) == null)
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

            FieldColumn column = FieldColumn.builderOf(name, fieldType)
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
        Class<?> fClass = f.getType();

        if (fClass.isAssignableFrom(Integer.class) || fClass.isAssignableFrom(int.class)) {
            return Type.INTEGER;
        } else if (fClass.isAssignableFrom(Double.class) || fClass.isAssignableFrom(double.class)) {
            return Type.DOUBLE;
        } else if (fClass.isAssignableFrom(Float.class) || fClass.isAssignableFrom(float.class)) {
            return Type.FLOAT;
        } else if (fClass.isAssignableFrom(String.class)) {
            return Type.TEXT;
        } else if (fClass.isAssignableFrom(Boolean.class) || fClass.isAssignableFrom(boolean.class)) {
            return Type.BOOLEAN;
        } else if (fClass.isAssignableFrom(Character.class) || fClass.isAssignableFrom(char.class)) {
            return Type.CHAR;
        } else if (fClass.isAssignableFrom(Short.class) || fClass.isAssignableFrom(short.class)) {
            return Type.SMALLINT;
        } else if (fClass.isAssignableFrom(Long.class) || fClass.isAssignableFrom(long.class)) {
            return Type.BIGINT;
        } else if (fClass.isAssignableFrom(java.util.Date.class) || fClass.isAssignableFrom(java.sql.Date.class) || fClass.isAssignableFrom(java.sql.Timestamp.class)) {
            return Type.DATETIME;
        } else if (fClass.isAssignableFrom(Enum.class)) {
            return Type.ENUM;
        } else {
            return Type.UNDEFINED;
        }
    }

    @Override
    public String toString() {
        columns.forEach(builder::add);
        return builder.toString();
    }
}
