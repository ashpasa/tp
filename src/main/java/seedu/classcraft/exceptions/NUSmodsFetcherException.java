package seedu.classcraft.exceptions;

public class NUSmodsFetcherException extends Exception {

    /**
     * Constructs a new NUSmodsFetcherException with no detail message.
     */
    public NUSmodsFetcherException() {
    }

    /**
     * Constructs a new NUSmodsFetcherException with the specified detail message.
     *
     * @param message The detail message.
     */
    public NUSmodsFetcherException(String message) {
        super(message);
    }
}
