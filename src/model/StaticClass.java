package model;

import java.lang.annotation.*;

/**
 * {@link StaticClass}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface StaticClass {
    String value() default "";

    Class<? extends Exception> exception() default Exception.class;
}
