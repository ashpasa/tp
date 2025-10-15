package seedu.duke.exceptions;

public class NUSmodsFetcherException extends Exception {
    public NUSmodsFetcherException() {
    }

    public NUSmodsFetcherException(String message) {
        super(message);
    }

    public NUSmodsFetcherException(String message, Throwable cause) {
        super(message, cause);
    }

    public NUSmodsFetcherException(Throwable cause) {
        super(cause);
    }

    public NUSmodsFetcherException(String message, Throwable cause,
                                  boolean enableSuppression,
                                  boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
