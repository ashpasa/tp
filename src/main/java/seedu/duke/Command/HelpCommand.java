package seedu.duke.Command;

public class HelpCommand extends Command{
    public HelpCommand() {
        super();
    }

    @Override
    public void executeCommand() {
        String userHelp = "Hi there, do you require help?\n"
                + "Here are the list of commands you can use:\n"
                + "1. add - to add a task\n"
                + "2. delete - to delete a task\n"
                + "3. confirm - to mark a task as done\n"
                + "4. view - to view all tasks\n"
                + "5. exit - to exit the program\n"
                + "6. help - to view this message again";
        System.out.println(userHelp);
    }
}
