package seedu.duke.command;

public class HelpCommand extends Command{
    public HelpCommand() {
        super();
    }

    @Override
    public void executeCommand() {
        String userHelp = "Hi there, do you require help?\n"
                + "Here are the list of commands you can use:\n"
                + "1. add — Adds a task. Format: add n/{MODULE_CODE} s/{SEMESTER} (SEMESTER: 1 to 8)\n"
                + "2. delete — Deletes a task. Format: delete {MODULE_CODE} \n"
                + "3. confirm — Confirm study plan\n"
                + "4. view — View all tasks/ Format: view {INFORMATION} (INFORMATION: plan,grad,sample\n"
                + "5. exit — Exit the program\n"
                + "6. help — View this message again";
        System.out.println(userHelp);
    }
}
