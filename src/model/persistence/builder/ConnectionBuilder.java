package model.persistence.builder;

import model.persistence.Driver;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * {@link ConnectionBuilder}
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
public class ConnectionBuilder extends AbstractSQLBuilder {
    private static final long serialVersionUID = 1L;

    private final Driver driver;
    private final HashMap<String, String> getters = new HashMap<>();

    private String host;
    private String database = "";
    private int port = -1;

    protected ConnectionBuilder(@NotNull final Driver driver) {
        this(driver, "localhost");
    }

    protected ConnectionBuilder(@NotNull final Driver driver, @NotNull final String host) {
        this.host = host;
        this.driver = driver;
    }

    public ConnectionBuilder set(@NotNull final String key, @NotNull final String value) {
        this.getters.put(key, value);
        return this;
    }

    public ConnectionBuilder databaseName(@NotNull final String database) {
        this.database = database;
        return this;
    }

    public ConnectionBuilder port(final int port) {
        this.port = port;
        return this;
    }

    public ConnectionBuilder host(@NotNull final String host) {
        this.host = host;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("jdbc:");
        builder.append(driver).append(":");

        switch (driver) {
            case MYSQL:
                builder.append("//").append(host).append(":").append(port == -1 ? 3306 : port);
                break;
            case H2:
                builder.append("tcp://").append(host).append(":").append(port == -1 ? 9092 : port);
                break;
            case SQLITE:
                return builder.append(host).toString();
        }

        if (!database.isEmpty() && !database.isBlank())
            builder.append("/").append(database);

        if (!getters.isEmpty()) {
            builder.append("?");
            appendMap(builder, getters, "=", "", "&");
        }


        return builder.toString();
    }
}
