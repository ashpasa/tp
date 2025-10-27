package seedu.classcraft.command;

import seedu.classcraft.storage.Storage;
import seedu.classcraft.studyplan.StudyPlan;
import seedu.classcraft.ui.Ui;

/**
 * Command abstract class representing a command to be executed. All command classes
 * should extend this class and implement the executeCommand method.
 */
public abstract class Command {

    /**
     * Abstract method to execute a command.
     * Command classes extending this class must implement executeCommand.
     *
     * @param studyPlan The current study plan ,including data restored from storage
     * @param ui The user interface to interact with the user
     * @param storage The storage handler to read/write data
     */
    public abstract void executeCommand(StudyPlan studyPlan,
                                        Ui ui,
                                        Storage storage) throws Exception;
}
