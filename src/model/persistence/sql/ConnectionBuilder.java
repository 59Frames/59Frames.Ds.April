package model.persistence.sql;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * {@link ConnectionBuilder}
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
public class ConnectionBuilder {
    private static final long serialVersionUID = 1;

    private final String host;
    private final int port;
    private final String persistenceType;
    private final String databaseType;
    private final HashMap<String, String> getters = new HashMap<>();

    private String database = "";

    public ConnectionBuilder(String persistenceType, String databaseType, String host, int port) {
        this.host = host;
        this.port = port;
        this.persistenceType = persistenceType;
        this.databaseType = databaseType;
    }

    public ConnectionBuilder set(@NotNull final String key, @NotNull final String value) {
        this.getters.put(key, value);
        return this;
    }

    public ConnectionBuilder databaseName(@NotNull final String database) {
        this.database = database;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(persistenceType)
                .append(":")
                .append(databaseType)
                .append("://")
                .append(host)
                .append(":")
                .append(port);

        if (!database.isEmpty() && !database.isBlank())
            builder.append("/").append(database);

        if (!getters.isEmpty()) {
            builder.append("?");
            final boolean[] first = new boolean[1];
            first[0] = true;

            getters.forEach((key, value) -> {
                if (!first[0])
                    builder.append("&");
                builder.append(key).append("=").append(value);
                first[0] = false;
            });
        }


        return builder.toString();
    }
}
