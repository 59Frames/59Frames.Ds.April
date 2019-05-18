package model.persistence.table;

/**
 * Type
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
public enum Type {
    UNDEFINED,
    CHAR,
    VARCHAR,
    TINYTEXT,
    TEXT,
    BLOB,
    MEDIUMTEXT,
    MEDIUMBLOB,
    LONGTEXT,
    LONGBLOB,
    TINYINT,
    SMALLINT,
    MEDIUMINT,
    INT,
    BIGINT,
    FLOAT,
    DOUBLE,
    DECIMAL,
    DATE,
    DATETIME,
    TIMESTAMP,
    TIME,
    ENUM,
    SET,
    BOOLEAN;

    private int length = -1;

    public int getLength() {
        return this.length;
    }

    public void setLength(final int length) {
        this.length = length;
    }
}
