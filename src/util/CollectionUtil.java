package util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * {@link CollectionUtil}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class CollectionUtil {
    public static <T> T pickRandom(@NotNull List<T> list) {
        return list.get(ThreadLocalRandom.current().nextInt(0, list.size()));
    }

    public static <T> T pickRandom(@NotNull T[] array) {
        return array[ThreadLocalRandom.current().nextInt(0, array.length)];
    }

    @NotNull
    public static <T> T[] removeRedundanciesFrom(@NotNull final T[] data) {
        HashSet<T> set = new HashSet<>(Arrays.asList(data));
        @SuppressWarnings("unchecked") final T[] a = (T[]) Array.newInstance(data[0].getClass(), set.size());
        return set.toArray(a);
    }

    @NotNull
    @Contract("_ -> new")
    public static <T> List<T> removeRedundanciesFrom(@NotNull final List<T> data) {
        return new ArrayList<>(new HashSet<>(data));
    }

    @Contract("_ -> param1")
    public static <T> T[] reverse(@NotNull final T[] data) {
        if (data.length < 2)
            return data;

        T[] clone = data.clone();
        for (int i = 0; i < clone.length; ++i) {
            data[i] = clone[clone.length - 1 - i];
        }

        return data;
    }

    @Contract("_ -> param1")
    public static <T> List<T> reverse(@NotNull final List<T> data) {
        if (data.size() < 2)
            return data;

        Collections.reverse(data);
        return data;
    }
}
