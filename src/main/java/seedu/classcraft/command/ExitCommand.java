package seedu.classcraft.command;

import seedu.classcraft.storage.Storage;
import seedu.classcraft.studyplan.StudyPlan;
import seedu.classcraft.ui.Ui;

/**
 * ExitCommand class representing the command to exit the application.
 */
public class ExitCommand extends Command {

    /**
     * Method from Command parent class.
     * Prints a exit message to the user.
     *
     * @param studyPlan The current study plan ,including data restored from storage
     * @param ui        The user interface to interact with the user
     * @param storage   The storage handler to read/write data
     */
    @Override
    public void executeCommand(StudyPlan studyPlan, Ui ui, Storage storage) {
        String exitMessage = "Thank you for using ClassCraft! See you next time!";
        ui.showMessage(exitMessage);
        System.exit(0);
    }
}
