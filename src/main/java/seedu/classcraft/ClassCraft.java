package seedu.classcraft;

import java.util.Scanner;

import seedu.classcraft.parser.Parser;
import seedu.classcraft.command.Command;
import seedu.classcraft.storage.Storage;
import seedu.classcraft.studyplan.StudyPlan;
import seedu.classcraft.ui.Ui;

public class ClassCraft {
    /**
     * Represents the entry-point for the java.duke.ClassCraft application.
     */
    public static String studyPlanFile = "./ClassCraftData/studyPlan.txt";

    /**
     * The main method to run the ClassCraft application.
     * 
     * @param args Command line arguments, should be empty.
     */
    public static void main(String[] args) {

        System.out.println("Hello from " + "ClassCraft");
        System.out.println("Input your command! Type 'help' if you need assistance.");

        Storage storage = new Storage(studyPlanFile);
        Ui ui = new Ui();

        StudyPlan currentStudyPlan = storage.restoreData(storage);
        System.out.println("Data restored successfully from ./ClassCraftData/studyPlan.txt");
        Scanner in = new Scanner(System.in);
        String userInput;


        while (true) {
            if (!in.hasNextLine()) {
                return;
            }
            userInput = in.nextLine();

            try {
                Parser parser = new Parser(userInput);
                Command command = parser.parseInput();

                command.executeCommand(currentStudyPlan, ui, storage);

            } catch (Exception e) {
                ui.showMessage("Error: " + e.getMessage());
            }

        }

    }
}
