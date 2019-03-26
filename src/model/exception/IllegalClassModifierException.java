package model.exception;

/**
 * {@link IllegalClassModifierException}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class IllegalClassModifierException extends Exception {
    public IllegalClassModifierException() {
        this("Class has an Illegal Modifier");
    }

    public IllegalClassModifierException(String message) {
        super(message);
    }
}
