package data.table;

import org.jetbrains.annotations.NotNull;

/**
 * {@link BlueprintColumn}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class BlueprintColumn {
    private final String name;
    private final Type type;
    private final String defaultValue;
    private final boolean notNull;
    private final boolean autoIncrement;
    private final boolean unique;
    private final boolean primaryKey;

    private BlueprintColumn(String name, Type type, String defaultValue, boolean notNull, boolean autoIncrement, boolean unique, boolean primaryKey) {
        this.name = name;
        this.type = type;
        this.defaultValue = defaultValue;
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(this.name);

        if (this.type.getLength() == -1) builder.append(" ").append(this.type.name());
        else builder.append(" ").append(this.type.name()).append("(").append(this.type.getLength()).append(")");

        if (!defaultValue.isEmpty() || !defaultValue.isBlank())
            builder.append(" ").append("DEFAULT").append(" ").append(this.defaultValue);
        if (autoIncrement) builder.append(" ").append("auto_increment");

        return builder.toString();
    }

    public static Builder builderOf(@NotNull final String name, @NotNull final Type type) {
        return new Builder(name, type);
    }

    public static class Builder {
        private final String name;
        private final Type type;

        private String defaultValue;
        private boolean notNull;
        private boolean autoIncrement;
        private boolean unique;
        private boolean primaryKey;

        private Builder(@NotNull final String name, @NotNull final Type type) {
            this.name = name;
            this.type = type;
            this.defaultValue = "";
            this.notNull = false;
            this.autoIncrement = false;
            this.unique = false;
            this.primaryKey = false;
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

        public BlueprintColumn build() {
            return new BlueprintColumn(name, type, defaultValue, notNull, autoIncrement, unique, primaryKey);
        }
    }
}
