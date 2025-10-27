package seedu.classcraft.command;

import seedu.classcraft.storage.Storage;
import seedu.classcraft.studyplan.StudyPlan;
import seedu.classcraft.ui.Ui;

public class AddCommand extends Command{
    public String[] moduleAddInfo;

    public AddCommand(String[] moduleAddInfo) {
        super();
        this.moduleAddInfo = moduleAddInfo;
    }

    @Override
    public void executeCommand(StudyPlan studyPlan, Ui ui, Storage storage) {
        try {
            studyPlan.addModule(moduleAddInfo[0], Integer.parseInt(moduleAddInfo[1]), storage, false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
