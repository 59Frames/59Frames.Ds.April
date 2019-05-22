package model.persistence;

import model.annotation.Table;
import model.persistence.builder.sql.CreateSQLBuilder;
import model.persistence.builder.sql.DeleteSQLBuilder;
import model.persistence.builder.sql.InsertSQLBuilder;
import model.persistence.builder.sql.UpdateSQLBuilder;
import model.concurrent.Promise;
import model.exception.MissingAnnotationException;
import model.interfaceable.Processable;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import util.Debugger;

import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * {@link EntityManager}
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
public final class EntityManager {

    private static final BaseContext db = Database.getInstance();

    private static volatile HashMap<Class<?>, HashMap<Integer, DatabaseObject>> cache = new HashMap<>();

    private EntityManager() {
    }

    public static <T extends DatabaseObject> int createTable(@NotNull final Class<T> tClass) {
        if (checkTableClass(tClass)) {
            try {
                CreateSQLBuilder builder = db.createCreateSQLBuilder(tClass.getAnnotation(Table.class).name());
                String sql = Blueprint.of(tClass).with(builder).toString();
                int result = db.runRawUpdate(sql);
                cache.put(tClass, createCacheMap());
                return result;
            } catch (SQLException e) {
                Debugger.exception(e);
                return -1;
            }
        } else {
            throw new MissingAnnotationException(String.format("Class<%s> is missing the @Table annotation", tClass.getSimpleName()));
        }
    }

    public static <T extends DatabaseObject> Promise<Integer> createTableAsync(@NotNull final Class<T> tClass) {
        return new Promise<>((Processable<Integer>) () -> createTable(tClass));
    }

    public static <T extends DatabaseObject> T insertOrUpdate(@NotNull final T t) throws SQLException {
        if (checkTableClass(t.getClass())) {
            return checkObjectExists(t)
                    ? update(t)
                    : insert(t);
        }

        return t;
    }

    public static <T extends DatabaseObject> Promise<T> insertOrUpdateAsync(@NotNull final T t) {
        if (checkTableClass(t.getClass())) {
            return checkObjectExists(t)
                    ? updateAsync(t)
                    : insertAsync(t);
        }

        return new Promise<>((Processable<T>) () -> t);
    }

    public static <T extends DatabaseObject> ArrayList<T> fetchAllFromDatabase(@NotNull final Class<T> tClass) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        HashSet<T> set = new HashSet<>();

        if (checkTableClass(tClass)) {
            checkCacheBase(tClass);

            String sql = db.createSelectBuilder(tClass.getDeclaredAnnotation(Table.class).name()).toString();
            JSONArray result = db.runRawQuery(sql);

            for (Object o : result) {
                T instance = createDatabaseObject(tClass, (JSONObject) o);
                set.add(instance);

                if (cache.containsKey(tClass)) {
                    cache.get(tClass).put(instance.getId(), instance);
                }
            }
        }

        return new ArrayList<>(set);
    }

    public static <T extends DatabaseObject> Promise<ArrayList<T>> fetchAllFromDatabaseAsync(@NotNull final Class<T> tClass) {
        return new Promise<>((Processable<ArrayList<T>>) () -> fetchAllFromDatabase(tClass));
    }

    public static <T extends DatabaseObject> T find(@NotNull final Class<T> tClass, int id) throws SQLException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        return findFirst(tClass, t -> t.getId() == id);
    }

    public static <T extends DatabaseObject> ArrayList<T> find(@NotNull final Class<T> tClass, @NotNull final Predicate<T> where) throws SQLException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Set<T> set = new HashSet<>();

        if (checkTableClass(tClass)) {
            checkCacheBase(tClass);

            set = findCachedPredicate(tClass, where);

            if (set.isEmpty()) {
                fetchAllFromDatabase(tClass);

                set = findCachedPredicate(tClass, where);
            }
        }

        return new ArrayList<>(set);
    }

    public static <T extends DatabaseObject> Promise<ArrayList<T>> findAsync(@NotNull final Class<T> tClass, @NotNull final Predicate<T> where) {
        return new Promise<>((Processable<ArrayList<T>>) () -> find(tClass, where));
    }

    public static <T extends DatabaseObject> Promise<T> findAsync(@NotNull final Class<T> tClass, final int id) {
        return new Promise<>((Processable<T>) () -> find(tClass, id));
    }

    public static <T extends DatabaseObject> T findFirst(@NotNull final Class<T> tClass, @NotNull final Predicate<T> where) throws SQLException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        ArrayList<T> result = find(tClass, where);

        if (result.isEmpty())
            throw new SQLException("No entries found");

        return result.get(0);
    }

    public static <T extends DatabaseObject> Promise<T> findFirstAsync(@NotNull final Class<T> tClass, @NotNull final Predicate<T> where) {
        return new Promise<>((Processable<T>) () -> findFirst(tClass, where));
    }

    public static <T extends DatabaseObject> int delete(@NotNull final T t) throws SQLException {
        return delete(t.getClass(), t.getId());
    }

    public static <T extends DatabaseObject> int delete(@NotNull final Class<T> tClass, final int id) throws SQLException {
        if (checkTableClass(tClass)) {
            if (checkObjectExists(tClass, id)) {
                checkCacheBase(tClass);

                final DeleteSQLBuilder builder = db.createDeleteBuilder(tClass.getAnnotation(Table.class).name());
                builder.where("id", "=", id);
                final String sql = builder.toString();

                int result = db.runRawUpdate(sql, statement -> statement.setInt(1, (int) builder.getValues().get(0)));

                cache.get(tClass).remove(id);

                return result;
            }
        }

        return -1;
    }

    public static <T extends DatabaseObject> Promise<Integer> deleteAsync(@NotNull final Class<T> tClass, final int id) {
        return new Promise<>((Processable<Integer>) () -> delete(tClass, id));
    }

    public static <T extends DatabaseObject> Promise<Integer> deleteAsync(@NotNull final T t) {
        return deleteAsync(t.getClass(), t.getId());
    }

    private static <T extends DatabaseObject> T update(@NotNull final T t) throws SQLException {
        if (checkObjectExists(t)) {
            checkCacheBase(t.getClass());

            if (cache.containsKey(t.getClass())) {
                cache.get(t.getClass()).put(t.getId(), t);
            }

            final UpdateSQLBuilder builder = db.createUpdateBuilder(t.getClass().getAnnotation(Table.class).name());

            t.update();

            Map<String, Object> map = t.toJSON().toMap();

            map.forEach(builder::set);
            builder.where("id", "=", t.getId());

            final String sql = builder.toString();

            db.runRawUpdate(sql, statement -> {
                int index = 1;

                for (Object o : builder.getSetValues()) {
                    bindParam(statement, index, o);
                    index++;
                }

                statement.setInt(index, (int) builder.getWhereValues().get(0));
            });
        }
        return t;
    }

    private static <T extends DatabaseObject> Promise<T> updateAsync(@NotNull final T t) {
        return new Promise<>((Processable<T>) () -> update(t));
    }

    private static <T extends DatabaseObject> T insert(@NotNull final T t) throws SQLException {
        if (!checkObjectExists(t)) {
            checkCacheBase(t.getClass());

            final InsertSQLBuilder builder = db.createInsertBuilder(t.getClass().getAnnotation(Table.class).name());
            Map<String, Object> map = t.toJSON().toMap();

            map.forEach(builder::set);

            final String sql = builder.toString();

            final int id = db.runRawInsert(sql, statement -> {
                int index = 1;
                for (Object o : builder.getValues()) {
                    bindParam(statement, index, o);
                    index++;
                }
            });

            t.setId(id);

            if (cache.containsKey(t.getClass())) {
                cache.get(t.getClass()).put(t.getId(), t);
            }
        }

        return t;
    }

    private static <T extends DatabaseObject> Promise<T> insertAsync(@NotNull final T t) {
        return new Promise<>((Processable<T>) () -> insert(t));
    }

    private static void bindParam(PreparedStatement statement, int index, Object o) throws SQLException {
        if (o instanceof Integer) {
            statement.setInt(index, (int) o);
        } else if (o instanceof Double) {
            statement.setDouble(index, (double) o);
        } else if (o instanceof Boolean) {
            statement.setBoolean(index, (boolean) o);
        } else if (o instanceof String) {
            statement.setString(index, (String) o);
        } else if (o instanceof Date) {
            statement.setDate(index, (Date) o);
        } else if (o instanceof Timestamp) {
            statement.setTimestamp(index, (Timestamp) o);
        } else if (o instanceof Float) {
            statement.setFloat(index, (float) o);
        } else {
            statement.setObject(index, o);
        }
    }

    private static <T extends DatabaseObject> T createDatabaseObject(@NotNull final Class<T> tClass, @NotNull final JSONObject object) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return tClass.getConstructor(JSONObject.class).newInstance(object);
    }

    private static <T extends DatabaseObject> boolean checkTableClass(@NotNull final Class<T> tClass) {
        return tClass.getDeclaredAnnotation(Table.class) != null;
    }

    private static <T extends DatabaseObject> void checkCacheBase(Class<T> aClass) {
        if (!cache.containsKey(aClass))
            cache.put(aClass, createCacheMap());
    }

    private static <T extends DatabaseObject> HashMap<Integer, T> createCacheMap() {
        return new HashMap<>();
    }

    private static <T extends DatabaseObject> boolean checkObjectExists(@NotNull final T t) {
        return checkObjectExists(t.getClass(), t.getId());
    }

    private static <T extends DatabaseObject> boolean checkObjectExists(@NotNull final Class<T> tClass, final int id) {
        checkCacheBase(tClass);
        if (cache.containsKey(tClass))
            return cache.get(tClass).containsKey(id);

        try {
            findFirst(tClass, t -> t.getId() == id);
            return true;
        } catch (Exception ignore) {
            return false;
        }
    }

    private static <T extends DatabaseObject> Set<T> findCachedPredicate(@NotNull final Class<T> tClass, @NotNull final Predicate<T> predicate) {
        checkCacheBase(tClass);
        return cache.get(tClass)
                .values()
                .parallelStream()
                .map(tClass::cast)
                .filter(predicate)
                .collect(Collectors.toSet());
    }
}
