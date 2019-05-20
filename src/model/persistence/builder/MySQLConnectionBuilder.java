package model.persistence.builder;

import org.jetbrains.annotations.NotNull;

/**
 * {@link MySQLConnectionBuilder}
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
public class MySQLConnectionBuilder extends ConnectionBuilder {
    public MySQLConnectionBuilder(@NotNull final String host, final int port) {
        super("jdbc", "mysql", host, port);
    }
}
