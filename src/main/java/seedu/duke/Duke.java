package seedu.duke;

import java.util.Scanner;

import seedu.duke.parser.Parser;
import seedu.duke.command.Command;
//import seedu.duke.studyplan.StudyPlan;

public class Duke {
    /**
     * Main entry-point for the java.duke.ClassCraft application.
     */

    public static void main(String[] args) {
        String logo = " ________  ___       ________  ________   ________  ________  __" +
                "______  ________  ________ _________   \n" +
                "|\\   ____\\|\\  \\     |\\   __  \\|\\   ____\\ |\\   ____\\|\\   ____\\|\\" +
                "   __  \\|\\   __  \\|\\  _____\\\\___   ___\\ \n" +
                "\\ \\  \\___|\\ \\  \\    \\ \\  \\|\\  \\ \\  \\___|_\\ \\  \\___|\\ \\  \\_" +
                "__|\\ \\  \\|\\  \\ \\  \\|\\  \\ \\  \\__/\\|___ \\  \\_| \n" +
                " \\ \\  \\    \\ \\  \\    \\ \\   __  \\ \\_____  \\\\ \\_____  \\ \\  \\    " +
                "\\ \\   _  _\\ \\   __  \\ \\   __\\    \\ \\  \\  \n" +
                "  \\ \\  \\____\\ \\  \\____\\ \\  \\ \\  \\|____|\\  \\\\|____|\\  \\ \\  \\____\\" +
                " \\  \\\\  \\\\ \\  \\ \\  \\ \\  \\_|     \\ \\  \\ \n" +
                "   \\ \\_______\\ \\_______\\ \\__\\ \\__\\____\\_\\  \\ ____\\_\\  \\ \\_______\\ \\" +
                "__\\\\ _\\\\ \\__\\ \\__\\ \\__\\       \\ \\__\\\n" +
                "    \\|_______|\\|_______|\\|__|\\|__|\\_________\\\\_________\\|_______|\\|__|" +
                "\\|__|\\|__|\\|__|\\|__|        \\|__|\n" +
                "                                 \\|_________\\|_________|         " +
                "                                      \n" +
                "                                                                " +
                "                                       \n" +
                "                                                                " +
                "                                       ";

        System.out.println("Hello from\n" + "Classcraft");
        System.out.println("Input your command! Type 'help' if you need assistance.");

        Scanner in = new Scanner(System.in);
        String userInput = "";

        //StudyPlan studyPlan = new StudyPlan(8);

        while (!"exit".equals(userInput)) {
            if (!in.hasNextLine()) {
                return;
            }
            userInput = in.nextLine();
            Parser parser = new Parser( userInput);
            Command command = parser.parseInput();
            command.executeCommand();

        }

    }
}
