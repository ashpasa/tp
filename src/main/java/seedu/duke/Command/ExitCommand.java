package seedu.duke.Command;

public class ExitCommand extends Command{
    public ExitCommand() {
        super();
    }

    @Override
    public void executeCommand() {
        System.out.println("Exiting the program. Goodbye!");
        System.exit(0);
    }
}
