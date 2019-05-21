package model.persistence.builder.conn;

import model.persistence.Driver;
import model.persistence.builder.ConnectionBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * {@link MySQLConnectionBuilder}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class MySQLConnectionBuilder extends ConnectionBuilder {

    public MySQLConnectionBuilder() {
        super(Driver.MYSQL);
    }

    public MySQLConnectionBuilder(@NotNull String host) {
        super(Driver.MYSQL, host);
    }
}
