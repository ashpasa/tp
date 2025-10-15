package seedu.duke.command;

import seedu.duke.StudyPlan.Grad;
import seedu.duke.StudyPlan.StudyPlan;
import seedu.duke.ui.Ui;

public class ViewGradReqCommand extends Command {

    @Override
    public void executeCommand(StudyPlan currentStudyPlan, Ui ui) throws Exception {
        String requirements = Grad.getAllRequirementsDisplay();

        ui.printMessage(requirements);
    }
}
