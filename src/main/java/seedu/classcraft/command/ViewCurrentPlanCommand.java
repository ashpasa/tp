package seedu.classcraft.command;

import seedu.classcraft.storage.Storage;
import seedu.classcraft.studyplan.StudyPlan;
import seedu.classcraft.ui.Ui;

public class ViewCurrentPlanCommand extends Command {
    public ViewCurrentPlanCommand() {
        super();
    }

    @Override
    public void executeCommand(StudyPlan studyPlan, Ui ui, Storage storage) throws Exception {
        ui.displayCurrentPlan(studyPlan);
    }
}
