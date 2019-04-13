package util;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@link Clearer}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public final class Clearer<T> {

    private Stream<T> stream;

    private Clearer(Stream<T> stream) {
        this.stream = stream;
    }

    public static <T> Clearer init(List<T> list) {
        return new Clearer<>(list.parallelStream());
    }

    public static <T> Clearer init(T[] arr) {
        return new Clearer<>(Arrays.stream(arr));
    }

    public Clearer<T> removeIf(@NotNull final Predicate<? super T>... filters) {
        var list = stream.collect(Collectors.toList());

        if (filters.length < 1)
            return this;

        for (var filter : filters)
            list.removeIf(filter);

        this.stream = list.parallelStream();
        return this;
    }

    public Clearer<T> removeRedundancies() {
        this.stream = this.stream.distinct();
        return this;
    }

    public List<T> toList() {
        return this.stream.collect(Collectors.toList());
    }

    public T[] toArray() {
        return CollectionUtil.asArray(toList());
    }
}
