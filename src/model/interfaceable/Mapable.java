package model.interfaceable;

import java.util.Map;

/**
 * Mapable is kind of an experimental interface
 * to simplify the database mapping
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
public interface Mapable<T extends Mapable<T>> {
    Map<String, Object> toMap();
    T fromMap(Map<String, Object> map);
}
