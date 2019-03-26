package model.annotation;

import java.lang.annotation.*;

/**
 * {@link StaticClass}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface StaticClass {
    String value() default "Illegal class modifier";
}
