package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * {@link DateUtil}
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
public class DateUtil {
    public static Date parse(String source, SimpleDateFormat format) {
        try {
            return format.parse(source);
        } catch (ParseException e) {
            Debugger.exception(e);
        }
        return Calendar.getInstance().getTime();
    }

    public static Date parse(String source, String format) {
        return parse(source, new SimpleDateFormat(format));
    }

    public static String format(Date date, SimpleDateFormat format) {
        return format.format(date);
    }

    public static String format(Date date, String format) {
        return format(date, new SimpleDateFormat(format));
    }

    public static java.util.Date now() {
        return Calendar.getInstance().getTime();
    }
}
