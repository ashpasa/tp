package seedu.duke.Exceptions;

import seedu.duke.Command.Command;

public class InvalidCommand extends Command {
    public InvalidCommand() {
        super();
    }

    @Override
    public void executeCommand() {
        System.out.println("I'm sorry, but I don't know what that means :-(");
    }
}
