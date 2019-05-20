package model.persistence.sql;

/**
 * {@link MySQLConnectionBuilder}
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
public class MySQLConnectionBuilder extends ConnectionBuilder {
    public MySQLConnectionBuilder(String host, int port) {
        super("jdbc", "mysql", host, port);
    }
}
