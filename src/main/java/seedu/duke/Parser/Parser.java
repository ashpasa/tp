package seedu.duke.Parser;

import seedu.duke.Command.Command;
import seedu.duke.Command.HelpCommand;
import seedu.duke.Exceptions.InvalidCommand;

public class Parser {
    public String commandType;

    public Parser(String userInput) {
        this.commandType = userInput;
    }

    public Command parseInput() {
        String command = commandType.toLowerCase();
        switch (command) {
        case "help":
            return new HelpCommand();
//        case "add":
//            System.out.println("Bye. Hope to see you again soon!");
//        case "delete":
//            System.out.println("Bye. Hope to see you again soon!");
//        case "confirm":
//            System.out.println("Bye. Hope to see you again soon!");
//        case "view":
//            System.out.println("Bye. Hope to see you again soon!");
//        case "exit":
//            System.out.println("Bye. Hope to see you again soon!");
        default:
            return new InvalidCommand();
        }
    }

    public void parseHelp() {
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
