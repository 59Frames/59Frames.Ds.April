package util;

import org.jetbrains.annotations.Contract;

import java.util.regex.Pattern;

/**
 * {@link ValidationUtil}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class ValidationUtil {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$");
    private static final Pattern IPV4_PATTERN = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
    private static final Pattern IPV6_STD_PATTERN = Pattern.compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");
    private static final Pattern IPV6_HEX_COMPRESSED_PATTERN = Pattern.compile("^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");
    private static final Pattern POSITIVE_DIGIT_PATTERN = Pattern.compile("^\\d*\\.?\\d+$");
    private static final Pattern NEGATIVE_DIGIT_PATTERN = Pattern.compile("^-\\d*\\.?\\d+$");
    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("^#?([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$");
    private static final Pattern DOMAIN_PATTERN = Pattern.compile("^([a-z][a-z0-9-]+(\\.|-*\\.))+[a-z]{2,6}$");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[^A-Za-z0-9]");

    public static boolean isEmail(final String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    @Contract(pure = true)
    public static boolean isEvenNumber(final double n) {
        return (n % 2) == 0;
    }

    @Contract(pure = true)
    public static boolean isOddNumber(final double n) {
        return (n % 2) != 0;
    }

    public static boolean isPositiveNumber(final String number) {
        return POSITIVE_DIGIT_PATTERN.matcher(number).matches();
    }

    public static boolean isNegativeNumber(final String number) {
        return NEGATIVE_DIGIT_PATTERN.matcher(number).matches();
    }

    public static boolean isNumber(final String number) {
        return isPositiveNumber(number) || isNegativeNumber(number);
    }

    public static boolean isIpv4(final String str) {
        return IPV4_PATTERN.matcher(str).matches();
    }

    public static boolean isIPv6StdAddress(final String input) {
        return IPV6_STD_PATTERN.matcher(input).matches();
    }

    public static boolean isIPv6HexCompressedAddress(final String input) {
        return IPV6_HEX_COMPRESSED_PATTERN.matcher(input).matches();
    }

    public static boolean isIpv6(final String input) {
        return isIPv6StdAddress(input) || isIPv6HexCompressedAddress(input);
    }

    public static boolean isHexColor(final String color) {
        return HEX_COLOR_PATTERN.matcher(color).matches();
    }

    public static boolean isDomain(final String domain) {
        return DOMAIN_PATTERN.matcher(domain).matches();
    }

    public static boolean isSpecialCharacter(String s) {
        return SPECIAL_CHAR_PATTERN.matcher(s).find();
    }
}
