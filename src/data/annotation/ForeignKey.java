package data.annotation;

import data.table.ConstraintRule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ForeignKey {
    String name();
    String targetTable();
    String from();
    String to();
    ConstraintRule updateRule();
    ConstraintRule deleteRule();
}
