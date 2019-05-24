package model.crypto;

import environment.Environment;
import org.jetbrains.annotations.NotNull;

import java.util.Base64;

/**
 * {@link Kryptonite}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public final class Kryptonite {

    private static final String KEY = Environment.get("kryptonite.key");
    private static final String PEPPER = Environment.get("kryptonite.pepper");
    private static final char[] KEYS = KEY.toCharArray();
    private static final Base64.Encoder ENCODER = Base64.getEncoder();
    private static final Base64.Decoder DECODER = Base64.getDecoder();

    private Kryptonite() {
    }

    public static String encrypt(@NotNull final String clear) {
        return base64encode(xorString(clear));
    }

    public static String encrypt(@NotNull final String clear, final int level) {
        String str = clear;

        for (int i = 0; i < level; i++)
            str = encrypt(str);

        return str;
    }

    public static String decrypt(@NotNull final String cipher) {
        return xorString(base64decode(cipher));
    }

    public static String decrypt(@NotNull final String cipher, final int level) {
        String str = cipher;

        for (int i = 0; i < level; i++)
            str = decrypt(str);

        return str;
    }

    private static String base64encode(String text) {
        return new String(ENCODER.encode(text.getBytes()));
    }

    private static String base64decode(String text) {
        return new String(DECODER.decode(text));
    }

    private static String xorString(@NotNull final String str) {
        char[] chars = str.toCharArray();
        char[] newChars = new char[chars.length];

        for (int i = 0; i < chars.length; i++) {
            newChars[i] = (char) (chars[i] ^ KEYS[i % KEYS.length]);
        }

        return new String(newChars);
    }
}
