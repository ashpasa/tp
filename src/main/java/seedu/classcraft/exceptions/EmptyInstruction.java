package seedu.classcraft.exceptions;

/**
 * Exception class for handling empty or incorrectly formatted instructions.
 */
public class EmptyInstruction extends Exception {

    /**
     * Constructor for EmptyInstruction exception.
     * Creates an exception with a message indicating
     * that the instruction format is incorrect.
     *
     * @param specificInstruction The specific instruction that has an incorrect format.
     */
    public EmptyInstruction(String specificInstruction) {
        super("The instruction format for " + specificInstruction + " is incorrect. ");

    }
}
