package seedu.duke.exceptions;

import seedu.duke.command.Command;

public class InvalidCommand extends Command {
    public InvalidCommand() {
        super();
    }

    @Override
    public void executeCommand() {
        System.out.println("I'm sorry, but I don't know what that means :-(");
    }
}
