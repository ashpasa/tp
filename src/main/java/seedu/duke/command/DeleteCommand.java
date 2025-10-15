package seedu.duke.command;

//import seedu.duke.studyplan.StudyPlan;

public class DeleteCommand extends Command{
    //private StudyPlan studyPlan;
    public String moduleToDelete;

    public DeleteCommand(String moduleCode) {
        super();
        this.moduleToDelete = moduleCode;
    }

    @Override
    public void executeCommand() {
        //studyPlan.removeModule(moduleToDelete);
    }
}
