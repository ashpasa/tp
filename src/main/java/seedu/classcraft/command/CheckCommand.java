package seedu.classcraft.command;

import seedu.classcraft.storage.Storage;
import seedu.classcraft.studyplan.StudyPlan;
import seedu.classcraft.ui.Ui;


/**
 * CheckCommand class representing the command to check for semesters with high workload.
 */
public class CheckCommand extends Command {
    public CheckCommand() {
        super();
    }

    @Override
    public void executeCommand(StudyPlan studyPlan, Ui ui, Storage storage) {
        ui.showMessage("Checking Study plan...");
        studyPlan.checkStudyPlan();
    }
}
