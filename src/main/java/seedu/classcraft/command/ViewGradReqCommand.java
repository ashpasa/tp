package seedu.classcraft.command;

import seedu.classcraft.storage.Storage;
import seedu.classcraft.studyplan.Grad;
import seedu.classcraft.studyplan.StudyPlan;
import seedu.classcraft.ui.Ui;

/**
 * ViewGradReqCommand class representing the command to view graduation requirements.
 */
public class ViewGradReqCommand extends Command {

    /**
     * Method from Command parent class.
     * Displays the graduation requirements to the user, from
     * the Grad class.
     *
     * @param studyPlan The current study plan, including data restored from storage.
     * @param ui        The user interface to interact with the user.
     * @param storage   The storage handler to read/write data.
     */
    @Override
    public void executeCommand(StudyPlan studyPlan, Ui ui, Storage storage) throws Exception {
        String requirements = Grad.getAllRequirementsDisplay();

        ui.showMessage(requirements);
    }
}
