package seedu.classcraft.command;

import seedu.classcraft.storage.Storage;
import seedu.classcraft.studyplan.Grad;
import seedu.classcraft.studyplan.StudyPlan;
import seedu.classcraft.ui.Ui;

public class ViewGradReqCommand extends Command {

    @Override
    public void executeCommand(StudyPlan studyPlan, Ui ui, Storage storage) throws Exception {
        String requirements = Grad.getAllRequirementsDisplay();

        ui.printMessage(requirements);
    }
}
