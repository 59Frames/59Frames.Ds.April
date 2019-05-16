package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * {@link DateUtil}
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
public class DateUtil {
    public static Date parse(String source, SimpleDateFormat format) throws ParseException {
        return format.parse(source);
    }

    public static Date parse(String source, String format) throws ParseException {
        return parse(source, new SimpleDateFormat(format));
    }
}
