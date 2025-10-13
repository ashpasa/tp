package seedu.duke;

import java.util.Scanner;
import seedu.duke.Parser.Parser;
import seedu.duke.Command.Command;

public class Duke {
    /**
     * Main entry-point for the java.duke.ClassCraft application.
     */
    public static void main(String[] args) {

        String logo = " ________  ___       ________  ________   ________  ________  ________  ________  ________ _________   \n" +
                "|\\   ____\\|\\  \\     |\\   __  \\|\\   ____\\ |\\   ____\\|\\   ____\\|\\   __  \\|\\   __  \\|\\  _____\\\\___   ___\\ \n" +
                "\\ \\  \\___|\\ \\  \\    \\ \\  \\|\\  \\ \\  \\___|_\\ \\  \\___|\\ \\  \\___|\\ \\  \\|\\  \\ \\  \\|\\  \\ \\  \\__/\\|___ \\  \\_| \n" +
                " \\ \\  \\    \\ \\  \\    \\ \\   __  \\ \\_____  \\\\ \\_____  \\ \\  \\    \\ \\   _  _\\ \\   __  \\ \\   __\\    \\ \\  \\  \n" +
                "  \\ \\  \\____\\ \\  \\____\\ \\  \\ \\  \\|____|\\  \\\\|____|\\  \\ \\  \\____\\ \\  \\\\  \\\\ \\  \\ \\  \\ \\  \\_|     \\ \\  \\ \n" +
                "   \\ \\_______\\ \\_______\\ \\__\\ \\__\\____\\_\\  \\ ____\\_\\  \\ \\_______\\ \\__\\\\ _\\\\ \\__\\ \\__\\ \\__\\       \\ \\__\\\n" +
                "    \\|_______|\\|_______|\\|__|\\|__|\\_________\\\\_________\\|_______|\\|__|\\|__|\\|__|\\|__|\\|__|        \\|__|\n" +
                "                                 \\|_________\\|_________|                                               \n" +
                "                                                                                                       \n" +
                "                                                                                                       ";

        System.out.println("Hello from\n" + logo);
        System.out.println("Input your command! Type 'help' if you need assistance.");

        String end_line = "end";
        Scanner in = new Scanner(System.in);

        String userInput = "";

        while (!(userInput.equals(end_line))) {

            userInput = in.nextLine();
            Parser parser = new Parser(userInput);
            Command command = parser.parseInput();
            command.executeCommand();

        }

    }
}
