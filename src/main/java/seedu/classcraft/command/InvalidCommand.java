package seedu.classcraft.command;

import seedu.classcraft.storage.Storage;
import seedu.classcraft.studyplan.StudyPlan;
import seedu.classcraft.ui.Ui;

public class InvalidCommand extends Command {
    public InvalidCommand() {
        super();
    }

    @Override
    public void executeCommand(StudyPlan studyPlan, Ui ui, Storage storage) {
        System.out.println("I'm sorry, but I don't know what that means :-(");
    }
}
