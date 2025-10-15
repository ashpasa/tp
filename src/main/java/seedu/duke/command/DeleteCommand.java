package seedu.duke.command;

//import seedu.duke.studyplan.StudyPlan;

import seedu.duke.studyplan.StudyPlan;
import seedu.duke.ui.Ui;

public class DeleteCommand extends Command{
    // private StudyPlan studyPlan;
    public String moduleToDelete;

    public DeleteCommand(String moduleCode) {
        super();
        this.moduleToDelete = moduleCode;
    }

    @Override
    public void executeCommand(StudyPlan studyPlan, Ui ui) {
        studyPlan.removeModule(moduleToDelete);
    }
}
