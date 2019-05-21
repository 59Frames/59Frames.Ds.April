package model.persistence.builder.conn;

import model.persistence.Driver;
import model.persistence.builder.ConnectionBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * {@link H2ConnectionBuilder}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class H2ConnectionBuilder extends ConnectionBuilder {
    public H2ConnectionBuilder() {
        super(Driver.H2);
    }

    public H2ConnectionBuilder(@NotNull String host) {
        super(Driver.H2, host);
    }
}
