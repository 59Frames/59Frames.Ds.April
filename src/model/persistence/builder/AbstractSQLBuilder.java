package model.persistence.builder;

import model.persistence.Driver;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * {@link AbstractSQLBuilder}
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class AbstractSQLBuilder {

    protected Driver dialect;

    public void dialect(Driver dialect) {
        this.dialect = dialect;
    }

    protected void appendList(@NotNull final StringBuilder builder, @NotNull final List<?> list, @NotNull final String init, @NotNull final String sep) {

        boolean first = true;

        for (Object s : list) {
            if (first) {
                builder.append(init);
            } else {
                builder.append(sep);
            }
            builder.append(s);
            first = false;
        }
    }

    protected void appendMap(@NotNull final StringBuilder builder, @NotNull final Map<?, ?> map, @NotNull final String mapSeparator, @NotNull final String init, @NotNull final String sep) {

        final boolean[] first = new boolean[1];
        first[0] = true;

        map.forEach((key, value) -> {
            builder.append(first[0] ? init : sep);

            builder.append(key).append(mapSeparator).append(value);

            first[0] = false;
        });
    }

}
