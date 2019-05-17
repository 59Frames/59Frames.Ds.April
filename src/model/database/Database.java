package model.database;

import environment.Environment;
import model.concurrent.Promise;
import model.interfaceable.Processable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.Debugger;
import util.Kryptonite;

import java.sql.*;

/**
 * {@link Database}
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
public class Database {
    private static final int DB_ENCRYPTION_LEVEL = Environment.getInteger("db.encryption.level");

    private static final String RAW_HOST = Environment.get("db.host");
    private static final int PORT = Environment.getInteger("db.port");
    private static final String DATABASE = Environment.get("db.database");
    private static final String HOST = String.format("jdbc:mysql://%s:%d/%s?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&allowMultiQueries=true", RAW_HOST, PORT, DATABASE);
    private static final String USERNAME = Kryptonite.decrypt(Environment.get("db.username"), DB_ENCRYPTION_LEVEL);
    private static final String PASSWORD = Kryptonite.decrypt(Environment.get("db.password"), DB_ENCRYPTION_LEVEL);

    private static Database instance = null;

    public static synchronized Database getInstance() {
        if (instance == null)
            instance = new Database();
        return instance;
    }

    private Database() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            Debugger.exception(e);
        }
    }

    public JSONArray runRawQuery(String query) throws SQLException {
        try (Connection connection = ConnectionHandler.createConnection(HOST, USERNAME, PASSWORD); PreparedStatement statement = connection.prepareStatement(query); ResultSet result = statement.executeQuery()) {
            if (validate(query)) return convert(result);
            else return new JSONArray();
        }
    }

    public Promise<JSONArray> runRawQueryAsync(String query) {
        return new Promise<>((Processable<JSONArray>) () -> runRawQuery(query));
    }

    public int runRawUpdate(String query) throws SQLException {
        try (Connection connection = ConnectionHandler.createConnection(HOST, USERNAME, PASSWORD); PreparedStatement statement = connection.prepareStatement(query)) {
            if (validate(query)) return statement.executeUpdate();
            else return -1;
        }
    }

    public int runRawInsert(String query) throws SQLException {
        try (Connection connection = ConnectionHandler.createConnection(HOST, USERNAME, PASSWORD); PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            if (validate(query)) {
                statement.executeUpdate();
                ResultSet rs = statement.getGeneratedKeys();
                if (rs != null && rs.next()) {
                    return (int) rs.getLong(1);
                } else return -1;
            } else return -1;
        }
    }

    public Promise<Integer> runRawUpdateAsync(String query) {
        return new Promise<>((Processable<Integer>) () -> runRawUpdate(query));
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

    private boolean validate(String sql) {
        try (Connection connection = ConnectionHandler.createConnection(HOST, USERNAME, PASSWORD)) {
            connection.prepareStatement(sql);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
