package seedu.classcraft;

import java.util.Scanner;

import seedu.classcraft.parser.Parser;
import seedu.classcraft.command.Command;
import seedu.classcraft.studyplan.StudyPlan;
import seedu.classcraft.ui.Ui;

public class ClassCraft {
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

        Ui ui = new Ui();
        StudyPlan currentStudyPlan = new StudyPlan(8);

        Scanner in = new Scanner(System.in);
        String userInput = "";


        while (!"exit".equals(userInput)) {
            if (!in.hasNextLine()) {
                return;
            }
            userInput = in.nextLine();

            try {
                Parser parser = new Parser(userInput);
                Command command = parser.parseInput();

                command.executeCommand(currentStudyPlan, ui);

            } catch (Exception e) {
                ui.printMessage("Error: " + e.getMessage());
            }

        }
        in.close();
    }
}
