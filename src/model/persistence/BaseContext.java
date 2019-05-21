package model.persistence;

import model.interfaceable.Binder;
import model.persistence.builder.sql.DeleteSQLBuilder;
import model.persistence.builder.sql.InsertSQLBuilder;
import model.persistence.builder.sql.SelectSQLBuilder;
import model.persistence.builder.sql.UpdateSQLBuilder;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;

/**
 * {@link BaseContext}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class BaseContext {
    private final Driver driver;
    private final String host;
    private final String username;
    private final String password;

    public BaseContext(Driver driver, String host, String username, String password) {
        try {
            Class.forName(driver._class);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.driver = driver;
        this.host = host;
        this.username = username;
        this.password = password;
    }

    public DeleteSQLBuilder createDeleteBuilder(@NotNull final String table) {
        DeleteSQLBuilder builder = new DeleteSQLBuilder(table);
        builder.dialect(this.driver);
        return builder;
    }

    public InsertSQLBuilder createInsertBuilder(@NotNull final String table) {
        InsertSQLBuilder builder = new InsertSQLBuilder(table);
        builder.dialect(this.driver);
        return builder;
    }

    public SelectSQLBuilder createSelectBuilder(@NotNull final String table) {
        SelectSQLBuilder builder = new SelectSQLBuilder(table);
        builder.dialect(this.driver);
        return builder;
    }

    public UpdateSQLBuilder createUpdateBuilder(@NotNull final String table) {
        UpdateSQLBuilder builder = new UpdateSQLBuilder(table);
        builder.dialect(this.driver);
        return builder;
    }

    public JSONArray runRawQuery(String query) throws SQLException {
        return runRawQuery(query, statement -> {
        });
    }

    public JSONArray runRawQuery(String query, Binder binder) throws SQLException {
        try (Connection connection = ConnectionManager.createConnection(this.host, this.username, this.password); PreparedStatement statement = connection.prepareStatement(query)) {
            if (validate(query)) {
                binder.bind(statement);
                ResultSet result = statement.executeQuery();
                JSONArray arr = convert(result);
                result.close();
                return arr;
            } else return new JSONArray();
        }
    }

    public int runRawUpdate(String query) throws SQLException {
        return runRawUpdate(query, statement -> {
        });
    }

    public int runRawUpdate(String query, Binder binder) throws SQLException {
        try (Connection connection = ConnectionManager.createConnection(this.host, this.username, this.password); PreparedStatement statement = connection.prepareStatement(query)) {
            if (validate(query)) {
                binder.bind(statement);
                return statement.executeUpdate();
            } else return -1;
        }
    }

    public int runRawInsert(String query) throws SQLException {
        return runRawInsert(query, statement -> {
        });
    }

    public int runRawInsert(String query, Binder binder) throws SQLException {
        try (Connection connection = ConnectionManager.createConnection(this.host, this.username, this.password); PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            if (validate(query)) {
                binder.bind(statement);
                statement.executeUpdate();
                ResultSet rs = statement.getGeneratedKeys();
                if (rs != null && rs.next()) {
                    return (int) rs.getLong(1);
                } else return -1;
            } else return -1;
        }
    }

    private boolean validate(String sql) {
        try (Connection connection = ConnectionManager.createConnection(this.host, this.username, this.password)) {
            connection.prepareStatement(sql);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private JSONArray convert(ResultSet rs) throws SQLException, JSONException {
        JSONArray json = new JSONArray();
        ResultSetMetaData metaData = rs.getMetaData();

        while (rs.next()) {
            int numColumns = metaData.getColumnCount();
            JSONObject obj = new JSONObject();

            for (int i = 1; i < numColumns + 1; i++) {
                String column_name = metaData.getColumnName(i);

                if (metaData.getColumnType(i) == java.sql.Types.ARRAY) {
                    obj.put(column_name, rs.getArray(column_name));
                } else if (metaData.getColumnType(i) == java.sql.Types.BIGINT) {
                    obj.put(column_name, rs.getInt(column_name));
                } else if (metaData.getColumnType(i) == java.sql.Types.BOOLEAN) {
                    obj.put(column_name, rs.getBoolean(column_name));
                } else if (metaData.getColumnType(i) == java.sql.Types.BLOB) {
                    obj.put(column_name, rs.getBlob(column_name));
                } else if (metaData.getColumnType(i) == java.sql.Types.DOUBLE) {
                    obj.put(column_name, rs.getDouble(column_name));
                } else if (metaData.getColumnType(i) == java.sql.Types.FLOAT) {
                    obj.put(column_name, rs.getFloat(column_name));
                } else if (metaData.getColumnType(i) == java.sql.Types.INTEGER) {
                    obj.put(column_name, rs.getInt(column_name));
                } else if (metaData.getColumnType(i) == java.sql.Types.NVARCHAR) {
                    obj.put(column_name, rs.getNString(column_name));
                } else if (metaData.getColumnType(i) == java.sql.Types.VARCHAR) {
                    obj.put(column_name, rs.getString(column_name));
                } else if (metaData.getColumnType(i) == java.sql.Types.TINYINT) {
                    obj.put(column_name, rs.getInt(column_name) == 1);
                } else if (metaData.getColumnType(i) == java.sql.Types.SMALLINT) {
                    obj.put(column_name, rs.getInt(column_name));
                } else if (metaData.getColumnType(i) == java.sql.Types.DATE) {
                    obj.put(column_name, rs.getDate(column_name));
                } else if (metaData.getColumnType(i) == java.sql.Types.TIMESTAMP) {
                    obj.put(column_name, rs.getTimestamp(column_name));
                } else {
                    obj.put(column_name, rs.getObject(column_name));
                }
            }

            json.put(obj);
        }

        return json;
    }
}

