package concurrent;

/**
 * {@link Result}
 *
 * @author Daniel Seifert
 * @version 1.0.0
 * @since 1.0.0
 */
class Result<T> {
    private final T result;
    private final Exception exception;
    private final boolean wasSuccessful;

    Result(T result, Exception exception) {
        this.result = result;
        this.exception = exception;
        this.wasSuccessful = this.result != null;
    }

    T getResult() {
        return result;
    }

    Exception getException() {
        return exception;
    }

    boolean wasSuccessful() {
        return wasSuccessful;
    }
}
