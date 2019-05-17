package model.database;

import org.jetbrains.annotations.NotNull;
import util.Debugger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * {@link ConnectionHandler}
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
public class ConnectionHandler {
    public static synchronized Connection createConnection(@NotNull final String host, @NotNull final String username, @NotNull final String password) {
        Connection connection;

        try {
            connection = DriverManager.getConnection(host, username, password);
        } catch (SQLException e) {
            Debugger.exception(e);
            connection = null;
        }

        return connection;
    }
}
