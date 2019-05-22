package model.persistence;

import org.jetbrains.annotations.NotNull;

/**
 * {@link FieldColumn}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class FieldColumn {
    private final String name;
    private final Type type;
    private final String defaultValue;
    private final int length;
    private final boolean notNull;
    private final boolean autoIncrement;
    private final boolean unique;
    private final boolean primaryKey;

    private FieldColumn(String name, Type type, String defaultValue, int length, boolean notNull, boolean autoIncrement, boolean unique, boolean primaryKey) {
        this.name = name;
        this.type = type;
        this.defaultValue = defaultValue;
        this.length = length;
        this.notNull = notNull;
        this.autoIncrement = autoIncrement;
        this.unique = unique;
        this.primaryKey = primaryKey;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public boolean isUnique() {
        return unique;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public String stringFor(Driver driver) {
        StringBuilder builder = new StringBuilder();
        builder.append(this.name);

        if (this.length == -1) builder.append(" ").append(this.type.name());
        else builder.append(" ").append(this.type.name()).append("(").append(this.length).append(")");

        if (notNull)
            builder.append(" ").append("not null");

        switch (driver) {
            case MYSQL: {
                if (autoIncrement) builder.append(" ").append("auto_increment");
                break;
            }
            case SQLITE: {
                if (primaryKey)
                    builder.append(" ").append("primary key");
                break;
            }
        }

        return builder.toString();
    }

    @Override
    public String toString() {
        return stringFor(Driver.MYSQL);
    }

    public static Builder builderOf(@NotNull final String name, @NotNull final Type type) {
        return new Builder(name, type);
    }

    public static class Builder {
        private final String name;
        private final Type type;

        private int length;
        private String defaultValue;
        private boolean notNull;
        private boolean autoIncrement;
        private boolean unique;
        private boolean primaryKey;

        private Builder(@NotNull final String name, @NotNull final Type type) {
            this.name = name;
            this.type = type;
            this.defaultValue = "";
            this.length = -1;
            this.notNull = false;
            this.autoIncrement = false;
            this.unique = false;
            this.primaryKey = false;
        }

        public Builder length(final int length) {
            this.length = length;
            return this;
        }

        public Builder defaultValue(@NotNull final String value) {
            this.defaultValue = value;
            return this;
        }

        public Builder required(final boolean value) {
            this.notNull = value;
            return this;
        }

        public Builder autoIncrement(final boolean value) {
            this.autoIncrement = value;
            return this;
        }

        public Builder unique(final boolean value) {
            this.unique = value;
            return this;
        }

        public Builder primaryKey(final boolean value) {
            this.primaryKey = value;
            return this;
        }

        public FieldColumn build() {
            return new FieldColumn(name, type, defaultValue, length, notNull, autoIncrement, unique, primaryKey);
        }
    }
}
