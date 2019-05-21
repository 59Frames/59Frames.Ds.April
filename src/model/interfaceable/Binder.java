package model.interfaceable;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface Binder {
    void bind(PreparedStatement statement) throws SQLException;
}
