package model.persistence.builder.conn;

import model.persistence.Driver;
import model.persistence.builder.ConnectionBuilder;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Paths;

/**
 * {@link SQLiteConnectionBuilder}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class SQLiteConnectionBuilder extends ConnectionBuilder {
    public SQLiteConnectionBuilder(@NotNull final String filepath) {
        super(Driver.SQLITE, "");
        if (!Paths.get(filepath).isAbsolute())
            throw new IllegalArgumentException(String.format("Filepath: \"%s\" is not absolute", filepath));
        host(filepath);
    }
}
