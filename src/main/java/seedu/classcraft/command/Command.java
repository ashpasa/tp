package seedu.classcraft.command;

import seedu.classcraft.storage.Storage;
import seedu.classcraft.studyplan.StudyPlan;
import seedu.classcraft.ui.Ui;

public abstract class Command {

    public abstract void executeCommand(StudyPlan studyPlan,
                                        Ui ui,
                                        Storage storage) throws Exception;
}
