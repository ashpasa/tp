package seedu.classcraft.command;


import seedu.classcraft.studyplan.StudyPlan;
import seedu.classcraft.ui.Ui;
import seedu.classcraft.exceptions.StudyPlanException;

public class AddCommand extends Command{

    public String[] moduleAddInfo;

    public AddCommand(String[] moduleAddInfo) {
        super();
        this.moduleAddInfo = moduleAddInfo;
    }

    @Override
    public void executeCommand(StudyPlan studyPlan, Ui ui) {
        try {
            studyPlan.addModule(moduleAddInfo[0], Integer.parseInt(moduleAddInfo[1]));
            ui.showMessage("Successfully added " + moduleAddInfo[0]
                    + " to semester " + moduleAddInfo[1]);
        } catch (StudyPlanException e) {
            // Handle prerequisite validation errors
            ui.showError("Prerequisite Error:\n" + e.getMessage());
        } catch (IllegalArgumentException e) {
            // Handle invalid semester errors
            ui.showError("Invalid semester: " + e.getMessage());
        } catch (Exception e) {
            // Handle other errors (API failures, etc.)
            ui.showError("Error adding module: " + e.getMessage());
        }
    }
}
