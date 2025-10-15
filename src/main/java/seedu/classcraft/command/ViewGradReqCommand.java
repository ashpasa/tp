package seedu.classcraft.command;

import seedu.classcraft.studyplan.Grad;
import seedu.classcraft.studyplan.StudyPlan;
import seedu.classcraft.ui.Ui;

public class ViewGradReqCommand extends Command {

    @Override
    public void executeCommand(StudyPlan currentStudyPlan, Ui ui) throws Exception {
        String requirements = Grad.getAllRequirementsDisplay();

        ui.printMessage(requirements);
    }
}
