package data;

import data.annotation.Table;
import data.sql.DeleteBuilder;
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
public class EntityManager {

    private static final Database db = Database.getInstance();

    private static volatile HashMap<Class<?>, HashMap<Integer, DatabaseObject>> cache = new HashMap<>();

    public static <T extends DatabaseObject> int createTable(@NotNull final Class<T> tClass) {
        if (checkTableClass(tClass)) {
            try {
                int result = db.runRawUpdate(Blueprint.of(tClass).toString());
                cache.put(tClass, createCacheMap());
                return result;
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

        return new Promise<T>((Processable<T>) () -> t);
    }

    public static <T extends DatabaseObject> ArrayList<T> getAll(@NotNull final Class<T> tClass) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ArrayList<T> list = new ArrayList<>();

        if (checkTableClass(tClass)) {
            checkCacheBase(tClass);

            String sql = new SelectBuilder().from(tClass.getDeclaredAnnotation(Table.class).name()).toString();
            JSONArray result = db.runRawQuery(sql);

            for (Object o : result) {
                T instance = createDatabaseObject(tClass, (JSONObject) o);
                list.add(instance);

                if (cache.containsKey(tClass)) {
                    cache.get(tClass).put(instance.getId(), instance);
                }
            }
        }

        return list;
    }

    public static <T extends DatabaseObject> Promise<ArrayList<T>> getAllAsync(@NotNull final Class<T> tClass) {
        return new Promise<>((Processable<ArrayList<T>>) () -> getAll(tClass));
    }

    public static <T extends DatabaseObject> T find(@NotNull final Class<T> tClass, int id) throws SQLException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        return findFirst(tClass, t -> t.getId() == id);
    }

    public static <T extends DatabaseObject> ArrayList<T> find(@NotNull final Class<T> tClass, @NotNull final Predicate<T> where) throws SQLException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Set<T> set = new HashSet<>();

        if (checkTableClass(tClass)) {
            checkCacheBase(tClass);

            set = findPredicate(tClass, where);

            if (set.isEmpty()) {
                getAll(tClass);

                set = findPredicate(tClass, where);
            }
        }

        return new ArrayList<>(set);
    }

    public static <T extends DatabaseObject> Promise<ArrayList<T>> findAsync(@NotNull final Class<T> tClass, @NotNull final Predicate<T> where) {
        return new Promise<>((Processable<ArrayList<T>>) () -> find(tClass, where));
    }

    public static <T extends DatabaseObject> Promise<T> findAsync(@NotNull final Class<T> tClass, int id) {
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

    public static <T extends DatabaseObject> int delete(@NotNull final Class<T> tClass, int id) throws SQLException {
        if (checkTableClass(tClass)) {
            if (checkObjectExists(tClass, id)) {
                checkCacheBase(tClass);

                String sql = new DeleteBuilder(tClass.getAnnotation(Table.class).name()).where(String.format("id = %d", id)).toString();
                int result = db.runRawUpdate(sql);

                cache.get(tClass).remove(id);

                return result;
            }
        }

        return -1;
    }

    public static <T extends DatabaseObject> Promise<Integer> deleteAsync(@NotNull final Class<T> tClass, int id) {
        return new Promise<>((Processable<Integer>) () -> delete(tClass, id));
    }

    public static <T extends DatabaseObject> Promise<Integer> deleteAsync(@NotNull final T t) {
        return new Promise<>((Processable<Integer>) () -> delete(t));
    }

    private static <T extends DatabaseObject> T update(@NotNull final T t) throws SQLException {
        if (checkObjectExists(t)) {
            checkCacheBase(t.getClass());

            if (cache.containsKey(t.getClass())) {
                cache.get(t.getClass()).put(t.getId(), t);
            }

            final UpdateBuilder builder = new UpdateBuilder(t.getClass().getAnnotation(Table.class).name());

            t.setLastUpdate(new Date(DateUtil.now().getTime()));

            Map<String, Object> map = t.toJSON().toMap();

            map.forEach((key, value) -> builder.set(String.format("%s = %s", key, SQLUtil.getSQLSyntaxValue(value))));
            builder.where("id = " + t.getId());

            String sql = builder.toString();

            db.runRawUpdate(sql);
        }
        return t;
    }

    private static <T extends DatabaseObject> Promise<T> updateAsync(@NotNull final T t) {
        return new Promise<>((Processable<T>) () -> update(t));
    }

    private static <T extends DatabaseObject> T insert(@NotNull final T t) throws SQLException {
        if (!checkObjectExists(t)) {
            checkCacheBase(t.getClass());

            final InsertBuilder builder = new InsertBuilder(t.getClass().getAnnotation(Table.class).name());
            Map<String, Object> map = t.toJSON().toMap();

            map.forEach((key, value) -> builder.set(key, SQLUtil.getSQLSyntaxValue(value)));

            String sql = builder.toString();

            final int id = db.runRawInsert(sql);

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

    private static <T extends DatabaseObject> T createDatabaseObject(@NotNull final Class<T> tClass, @NotNull final JSONObject object) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return tClass.getConstructor(JSONObject.class).newInstance(object);
    }

    private static <T extends DatabaseObject> boolean checkTableClass(@NotNull final Class<T> tClass) {
        return tClass.getDeclaredAnnotation(Table.class) != null;
    }

    private static void checkCacheBase(Class<? extends DatabaseObject> aClass) {
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
        if (cache.containsKey(tClass))
            return cache.get(tClass).containsKey(id);

        try {
            findFirst(tClass, t -> t.getId() == id);
            return true;
        } catch (Exception ignore) {
            return false;
        }
    }

    private static <T extends DatabaseObject> Set<T> findPredicate(@NotNull final Class<T> tClass, @NotNull final Predicate<T> predicate) {
        checkCacheBase(tClass);
        return cache.get(tClass)
                .values()
                .parallelStream()
                .map(tClass::cast)
                .filter(predicate)
                .collect(Collectors.toSet());
    }
}
