package util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Objects;

/**
 * {@link FileUtil}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class FileUtil {

    private static final ClassLoader classLoader = ClassLoader.getSystemClassLoader();

    @NotNull
    @Contract("_ -> new")
    public static File load(String filepath) {
        return new File(Objects.requireNonNull(classLoader.getResource(filepath)).getFile());
    }

    public static void serialize(@NotNull final String path, @NotNull final Object obj) throws Exception {
        final FileOutputStream fileOut = new FileOutputStream(load(path));
        final ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(obj);
        out.close();
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
        char ch;
        int len;
        if(fileName==null ||
                (len = fileName.length())==0 ||
                (ch = fileName.charAt(len-1))=='/' || ch=='\\' || //in the case of a directory
                ch=='.' ) //in the case of . or ..
            return "";
        int dotInd = fileName.lastIndexOf('.'),
                sepInd = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
        if( dotInd<=sepInd )
            return "";
        else
            return fileName.substring(dotInd+1).toLowerCase();
    }

    @NotNull
    public static String getFileExtension(@NotNull File file) {
        return getFileExtension(file.getName());
    }
}
