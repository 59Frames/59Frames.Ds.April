package util;

import java.sql.Date;

/**
 * {@link SQLUtil}
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
public class SQLUtil {
    public static String getSQLSyntaxValue(Object o) {
        String fieldType = o.getClass().getSimpleName();

        switch (fieldType) {
            case "char":
            case "String":
                return String.format("'%s'", String.valueOf(o));
            case "Timestamp":
            case "Date":
                return String.format("'%s'", DateUtil.format(((Date) o), "yyyy-MM-dd HH:mm:ss"));
            case "boolean":
                return Boolean.parseBoolean(String.valueOf(o)) ? "1" : "0";
            default:
                return String.valueOf(o);
        }
    }
}
