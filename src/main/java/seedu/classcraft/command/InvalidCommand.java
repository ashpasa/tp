package seedu.classcraft.command;

import seedu.classcraft.storage.Storage;
import seedu.classcraft.studyplan.StudyPlan;
import seedu.classcraft.ui.Ui;

/**
 * InvalidCommand class representing an invalid command input by the user.
 */
public class InvalidCommand extends Command {

    /**
     * Method from Command parent class.
     * Prints out a message to user indicating that their input is not valid.
     *
     * @param studyPlan The current study plan ,including data restored from storage
     * @param ui The user interface to interact with the user
     * @param storage The storage handler to read/write data
     */
    @Override
    public void executeCommand(StudyPlan studyPlan, Ui ui, Storage storage) {
        String invalidMessage = "I'm sorry, but I don't know what that means :-(." +
                " Please type 'help' to see the list of available commands.";
        ui.printMessage(invalidMessage);
    }
}
