package seedu.classcraft.command;

import seedu.classcraft.storage.Storage;
import seedu.classcraft.studyplan.StudyPlan;
import seedu.classcraft.ui.Ui;

public class DeleteCommand extends Command{
    public String moduleToDelete;

    public DeleteCommand(String moduleCode) {
        super();
        this.moduleToDelete = moduleCode;
    }

    @Override
    public void executeCommand(StudyPlan studyPlan, Ui ui, Storage storage) {
        studyPlan.removeModule(moduleToDelete, storage);
    }
}
