package model.persistence;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.Debugger;

import java.sql.*;

/**
 * {@link BaseContext}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class BaseContext {
    private final String host;
    private final String username;
    private final String password;

    public BaseContext(String host, String username, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            Debugger.exception(e);
        }

        this.host = host;
        this.username = username;
        this.password = password;
    }


    public JSONArray runRawQuery(String query) throws SQLException {
        try (Connection connection = ConnectionHandler.createConnection(this.host, this.username, this.password); PreparedStatement statement = connection.prepareStatement(query); ResultSet result = statement.executeQuery()) {
            if (validate(query)) {
                return convert(result);
            } else return new JSONArray();
        }
    }

    public int runRawUpdate(String query) throws SQLException {
        try (Connection connection = ConnectionHandler.createConnection(this.host, this.username, this.password); PreparedStatement statement = connection.prepareStatement(query)) {
            if (validate(query)) {
                return statement.executeUpdate();
            } else return -1;
        }
    }

    public int runRawInsert(String query) throws SQLException {
        try (Connection connection = ConnectionHandler.createConnection(this.host, this.username, this.password); PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            if (validate(query)) {
                statement.executeUpdate();
                ResultSet rs = statement.getGeneratedKeys();
                if (rs != null && rs.next()) {
                    return (int) rs.getLong(1);
                } else return -1;
            } else return -1;
        }
    }

    private boolean validate(String sql) {
        try (Connection connection = ConnectionHandler.createConnection(this.host, this.username, this.password)) {
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
