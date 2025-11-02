package seedu.classcraft.command;

import seedu.classcraft.storage.Storage;
import seedu.classcraft.studyplan.StudyPlan;
import seedu.classcraft.ui.Ui;

public class BalanceCommand extends Command {
    public BalanceCommand() {
        super();
    }

    @Override
    public void executeCommand(StudyPlan studyPlan, Ui ui, Storage storage) {
        ui.showMessage("Study plan balancing");
        studyPlan.balanceStudyPlan();
    }
}
