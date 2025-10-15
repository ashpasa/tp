package seedu.duke.command;

import seedu.duke.studyplan.Grad;
import seedu.duke.studyplan.StudyPlan;
import seedu.duke.ui.Ui;

public class ViewGradReqCommand extends Command {

    @Override
    public void executeCommand(StudyPlan currentStudyPlan, Ui ui) throws Exception {
        String requirements = Grad.getAllRequirementsDisplay();

        ui.printMessage(requirements);
    }
}
