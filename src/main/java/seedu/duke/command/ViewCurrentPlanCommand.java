package seedu.duke.command;

import seedu.duke.studyplan.StudyPlan;
import seedu.duke.ui.Ui;

public class ViewCurrentPlanCommand extends Command {
    public ViewCurrentPlanCommand() {
        super();
    }

    @Override
    public void executeCommand(StudyPlan studyPlan, Ui ui) throws Exception {
        ui.displayCurrentPlan(studyPlan);
    }
}
