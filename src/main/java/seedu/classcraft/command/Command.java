package seedu.classcraft.command;

import seedu.classcraft.studyplan.StudyPlan;
import seedu.classcraft.ui.Ui;

public abstract class Command {

    public abstract void executeCommand(StudyPlan studyPlan, Ui ui) throws Exception;
}
