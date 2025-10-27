package seedu.classcraft.command;

import seedu.classcraft.studyplan.StudyPlan;
import seedu.classcraft.ui.Ui;

public class BalanceCommand extends Command {
    public BalanceCommand() {
        super();
    }

    @Override
    public void executeCommand(StudyPlan studyPlan, Ui ui) {
        studyPlan.balanceStudyPlan();
    }
}
