package model.persistence;

import environment.Environment;
import model.persistence.builder.MySQLConnectionBuilder;
import util.Kryptonite;

/**
 * {@link Database}
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
public class Database extends BaseContext {
    private static final int DB_ENCRYPTION_LEVEL = Environment.getInteger("db.encryption.level");

    private static final String DOMAIN = Environment.get("db.host");
    private static final int PORT = Environment.getInteger("db.port");
    private static final String DATABASE = Environment.get("db.database");
    private static final String HOST = buildHost();
    private static final String USERNAME = Kryptonite.decrypt(Environment.get("db.username"), DB_ENCRYPTION_LEVEL);
    private static final String PASSWORD = Kryptonite.decrypt(Environment.get("db.password"), DB_ENCRYPTION_LEVEL);

    private static Database instance = null;

    public static synchronized Database getInstance() {
        if (instance == null)
            instance = new Database();
        return instance;
    }

    private Database() {
        super(HOST, USERNAME, PASSWORD);
    }

    private static String buildHost() {
        return new MySQLConnectionBuilder(DOMAIN, PORT)
                .databaseName(DATABASE)
                .set("useUnicode", "true")
                .set("useJDBCCompliantTimezoneShift", "true")
                .set("useLegacyDatetimeCode", "false")
                .set("serverTimezone", "Europe/Zurich")
                .set("allowMultiQueries", "true")
                .toString();
    }
}