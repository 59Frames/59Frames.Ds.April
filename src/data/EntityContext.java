package data;

import data.annotation.Table;
import data.sql.InsertBuilder;
import data.sql.SelectBuilder;
import data.sql.UpdateBuilder;
import data.table.Blueprint;
import data.table.DatabaseObject;
import model.concurrent.Promise;
import model.interfaceable.Processable;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import util.DateUtil;
import util.Debugger;
import util.SQLUtil;

import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

/**
 * {@link EntityContext}
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
public class EntityContext {

    private static final Database db = Database.getInstance();

    public static <T extends DatabaseObject> int createTable(@NotNull final Class<T> tClass) {
        if (checkTableClass(tClass)) {
            try {
                return db.runRawUpdate(Blueprint.of(tClass).toString());
            } catch (SQLException e) {
                Debugger.warning(String.format("Table %s already exists", tClass.getAnnotation(Table.class).name()));
                return -1;
            }
        }
        return -1;
    }

    public static <T extends DatabaseObject> Promise<Integer> createTableAsync(@NotNull final Class<T> tClass) {
        return new Promise<>((Processable<Integer>) () -> createTable(tClass));
    }

    public static <T extends DatabaseObject> T insertOrUpdate(T t) throws SQLException {
        if (checkTableClass(t.getClass())) {

            String table = t.getClass().getAnnotation(Table.class).name();

            if (checkObjectExists(t.getClass(), t.getId())) {
                final UpdateBuilder builder = new UpdateBuilder(table);

                t.setLastUpdate(new Date(DateUtil.now().getTime()));

                Map<String, Object> map = t.toJSON().toMap();

                map.forEach((key, value) -> builder.set(String.format("%s = %s", key, SQLUtil.getSQLSyntaxValue(value))));
                builder.where("id = " + t.getId());

                String sql = builder.toString();

                db.runRawUpdate(sql);
                return t;
            } else {
                final InsertBuilder builder = new InsertBuilder(table);
                Map<String, Object> map = t.toJSON().toMap();

                map.forEach((key, value) -> builder.set(key, SQLUtil.getSQLSyntaxValue(value)));

                String sql = builder.toString();

                final int id = db.runRawInsert(sql);

                t.setId(id);

                return t;
            }
        }

        return t;
    }

    public static <T extends DatabaseObject> ArrayList<T> getAll(@NotNull final Class<T> tClass) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ArrayList<T> list = new ArrayList<>();

        if (checkTableClass(tClass)) {
            String sql = new SelectBuilder().from(tClass.getDeclaredAnnotation(Table.class).name()).toString();
            JSONArray result = db.runRawQuery(sql);

            for (Object o : result) {
                list.add(createDatabaseObject(tClass, (JSONObject) o));
            }
        }

        return list;
    }

    public static <T extends DatabaseObject> Promise<ArrayList<T>> getAllAsync(@NotNull final Class<T> tClass) {
        return new Promise<>((Processable<ArrayList<T>>) () -> getAll(tClass));
    }

    public static <T extends DatabaseObject> T find(@NotNull final Class<T> tClass, int id) throws SQLException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        return findFirst(tClass, String.format("id = %d", id));
    }

    public static <T extends DatabaseObject> ArrayList<T> find(@NotNull final Class<T> tClass, @NotNull final String where) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ArrayList<T> list = new ArrayList<>();

        if (checkTableClass(tClass)) {

            String sql = new SelectBuilder()
                    .from(tClass.getDeclaredAnnotation(Table.class).name())
                    .where(where)
                    .toString();

            JSONArray result = db.runRawQuery(sql);

            for (Object o : result) {
                list.add(createDatabaseObject(tClass, (JSONObject) o));
            }
        }

        return list;
    }

    public static <T extends DatabaseObject> Promise<ArrayList<T>> findAsync(@NotNull final Class<T> tClass, @NotNull final String where) {
        return new Promise<>((Processable<ArrayList<T>>) () -> find(tClass, where));
    }

    public static <T extends DatabaseObject> Promise<T> findAsync(@NotNull final Class<T> tClass, int id) {
        return new Promise<>((Processable<T>) () -> find(tClass, id));
    }

    public static <T extends DatabaseObject> T findFirst(@NotNull final Class<T> tClass, @NotNull final String where) throws SQLException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        ArrayList<T> result = find(tClass, where);

        if (result.isEmpty())
            throw new SQLException("No entries found");

        return result.get(0);
    }

    public static <T extends DatabaseObject> Promise<T> findFirstAsync(@NotNull final Class<T> tClass, @NotNull final String where) {
        return new Promise<>((Processable<T>) () -> findFirst(tClass, where));
    }

    private static <T extends DatabaseObject> T createDatabaseObject(@NotNull final Class<T> tClass, @NotNull final JSONObject object) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return tClass.getConstructor(JSONObject.class).newInstance(object);
    }

    private static <T extends DatabaseObject> boolean checkTableClass(@NotNull final Class<T> tClass) {
        return tClass.getDeclaredAnnotation(Table.class) != null;
    }

    private static <T extends DatabaseObject> boolean checkObjectExists(@NotNull final Class<T> tClass, int id) {
        try {
            findFirst(tClass, String.format("id = %d", id));
            return true;
        } catch (Exception ignore) {
            return false;
        }
    }
}
