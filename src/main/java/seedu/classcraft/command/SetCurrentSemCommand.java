package seedu.classcraft.command;

import seedu.classcraft.studyplan.StudyPlan;
import seedu.classcraft.ui.Ui;
import seedu.classcraft.storage.Storage;

/**
 * A command to set the current semester.
 * All modules in semesters before the specified semester will be marked as COMPLETED.
 */
public class SetCurrentSemCommand extends Command {

    private int semester;

    public SetCurrentSemCommand(int semester) {
        this.semester = semester;
    }

    @Override
    public void executeCommand(StudyPlan studyPlan, Ui ui, Storage storage) {
        try {
            int modulesCompleted = studyPlan.setCurrentSemester(semester, storage);

            ui.printMessage("Current semester set to " + semester + ".\n"
                    + modulesCompleted + " modules from previous semesters (1 to " + (semester - 1)
                    + ") have been marked as COMPLETED.");

        } catch (Exception e) {
            ui.printMessage("Error: " + e.getMessage());
        }
    }
}