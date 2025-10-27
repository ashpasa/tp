package seedu.classcraft.exceptions;

public class EmptyInstruction extends Exception{
    public EmptyInstruction( String specificInstruction) {
        super("OOPS!!! The instruction format for " + specificInstruction + " is incorrect. " +
                "Check 'help' for guidance.");

    }
}
