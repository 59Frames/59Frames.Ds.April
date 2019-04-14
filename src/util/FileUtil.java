package util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * {@link FileUtil}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public final class FileUtil {

    private FileUtil() {
    }

    private static final ClassLoader classLoader = ClassLoader.getSystemClassLoader();

    @NotNull
    @Contract("_ -> new")
    public static File load(String filepath) {
        return isAbsolutePath(filepath)
                ? new File(filepath)
                : new File(Objects.requireNonNull(classLoader.getResource(filepath)).getFile());
    }

    public static boolean isAbsolutePath(@NotNull final String path) {
        return Paths.get(path).isAbsolute();
    }

    public static void serialize(@NotNull final String path, @NotNull final Serializable obj) throws Exception {
        final FileOutputStream fileOut = new FileOutputStream(load(path));
        final ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(obj);
        out.flush();
        out.close();
        fileOut.flush();
        fileOut.close();
    }

    public static <T> T deserialize(@NotNull final String path, Class<T> tClass) throws Exception {
        return tClass.cast(deserialize(path));
    }

    public static Object deserialize(@NotNull final String path) throws Exception {
        final FileInputStream fileIn = new FileInputStream(load(path));
        final ObjectInputStream in = new ObjectInputStream(fileIn);
        Object result = in.readObject();
        in.close();
        fileIn.close();
        return result;
    }

    @NotNull
    public static String getFileExtension(String fileName) {
        final char ch;
        final int len;
        if (fileName == null || (len = fileName.length()) == 0 || (ch = fileName.charAt(len - 1)) == '/' || ch == '\\' || ch == '.')
            return "";
        int dotInd = fileName.lastIndexOf('.'), sepInd = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

        return dotInd <= sepInd
                ? ""
                : fileName.substring(dotInd + 1).toLowerCase();
    }

    @NotNull
    public static String getFileExtension(@NotNull File file) {
        return getFileExtension(file.getName());
    }
}
