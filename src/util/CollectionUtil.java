package util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static util.Toolbox.*;

/**
 * {@link CollectionUtil}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public final class CollectionUtil {

    private CollectionUtil(){
    }

    @NotNull
    public static double[] createRandomDoubleArray(final int size, final double min, final double max){
        return createRandomDoubleArray(size, min, max, 16);
    }

    @NotNull
    public static double[] createRandomDoubleArray(final int size, final double min, final double max, final int fractionalPlaces){
        if(size < 1){
            return new double[size];
        }
        double[] ar = new double[size];
        for(int i = 0; i < size; i++){
            ar[i] = round(random(min, max), fractionalPlaces);
        }
        return ar;
    }

    public static int[] createRandomIntegerArray(final int size, final int lower_bound, final int upper_bound){
        if(size < 1){
            return null;
        }
        int[] ar = new int[size];
        for(int i = 0; i < size; i++){
            ar[i] = integer(random(lower_bound,upper_bound));
        }
        return ar;
    }

    public static double[][] createRandomDoubleMatrix(final int sizeX, final int sizeY, final double lower_bound, final double upper_bound){
        if(sizeX < 1 || sizeY < 1){
            return null;
        }
        double[][] ar = new double[sizeX][sizeY];
        for(int i = 0; i < sizeX; i++){
            ar[i] = createRandomDoubleArray(sizeY, lower_bound, upper_bound);
        }
        return ar;
    }

    public static <T> T pickRandom(@NotNull List<T> list) {
        return list.get(ThreadLocalRandom.current().nextInt(0, list.size()));
    }

    public static <T> T pickRandom(@NotNull T[] array) {
        return array[ThreadLocalRandom.current().nextInt(0, array.length)];
    }

    @NotNull
    public static <T> T[] removeRedundanciesFrom(@NotNull final T[] data) {
        return asArray(Arrays.stream(data).distinct().collect(Collectors.toList()));
    }

    @NotNull
    @Contract("_ -> new")
    public static <T> List<T> removeRedundanciesFrom(@NotNull final List<T> data) {
        return data.parallelStream().distinct().collect(Collectors.toList());
    }

    @Contract("_ -> param1")
    public static <T> T[] reverse(@NotNull final T[] data) {
        if (data.length < 2)
            return data;

        return streamReverse(Arrays.stream(data)).toArray(size -> data.clone());
    }

    @Contract("_ -> param1")
    public static <T> List<T> reverse(@NotNull final List<T> data) {
        if (data.size() < 2)
            return data;

        return streamReverse(data.parallelStream()).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private static <T> Stream<T> streamReverse(Stream<T> input) {
        Object[] temp = input.toArray();
        return (Stream<T>) IntStream.range(0, temp.length).mapToObj(i -> temp[temp.length - i - 1]);
    }

    @NotNull
    public static <T> List<? extends T> clearAndRemoveIf(@NotNull final List<T> list, @NotNull final Predicate<? super T> filter) {
        final var clearedList = removeRedundanciesFrom(list);
        clearedList.removeIf(filter);
        return clearedList;
    }

    @SafeVarargs
    @NotNull
    public static <T> List<? extends T> clearAndRemoveIf(@NotNull final List<? extends T> list, @NotNull final Predicate<? super T>... filters) {
        final var clearedList = removeRedundanciesFrom(list);

        if (filters.length < 1)
            return clearedList;

        for (var filter : filters)
            clearedList.removeIf(filter);

        return clearedList;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] asArray(@NotNull final List<T> list) {
        if (list.isEmpty())
            throw new IllegalArgumentException("List is empty");

        T[] arr = (T[]) Array.newInstance(list.get(0).getClass(), list.size());
        for (int i = 0; i < list.size(); ++i) {
            arr[i] = list.get(i);
        }
        return arr;
    }

    public static <T> List<T> asList(@NotNull final T[] arr) {
        return Arrays.asList(arr);
    }
}
