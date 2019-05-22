package model.persistence;

import org.jetbrains.annotations.NotNull;

public enum Driver {
    MYSQL("com.mysql.cj.jdbc.Driver"),
    SQLITE("org.sqlite.JDBC");

    public final String _class;

    Driver(@NotNull final String _class) {
        this._class = _class;
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
