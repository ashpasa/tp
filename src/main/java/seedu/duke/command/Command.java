package seedu.duke.command;

import seedu.duke.studyplan.StudyPlan;
import seedu.duke.ui.Ui;

public abstract class Command {

    public abstract void executeCommand(StudyPlan studyPlan, Ui ui) throws Exception;
}
