package seedu.classcraft.command;

import seedu.classcraft.storage.Storage;
import seedu.classcraft.studyplan.StudyPlan;
import seedu.classcraft.ui.Ui;


/**
 * CheckCommand class representing the command to check for semesters with high workload.
 */
public class CheckCommand extends Command {

    /**
     * Constructor for CheckCommand.
     */
    public CheckCommand() {
        super();
    }

    /**
     * Executes the check command to identify semesters with high workload.
     * 
     * @param studyPlan The StudyPlan object containing the semesters and modules.
     * @param ui        The Ui object for user interaction.
     * @param storage   The Storage object for data persistence.
     */
    @Override
    public void executeCommand(StudyPlan studyPlan, Ui ui, Storage storage) {
        ui.showMessage("Checking Study plan...");
        studyPlan.checkStudyPlan();
    }
}
