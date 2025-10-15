package seedu.duke.command;

import seedu.duke.StudyPlan.StudyPlan;
import seedu.duke.ui.Ui;

public abstract class Command {

    public abstract void executeCommand(StudyPlan studyPlan, Ui ui) throws Exception;
}
