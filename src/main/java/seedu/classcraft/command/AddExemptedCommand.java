package seedu.classcraft.command;

import seedu.classcraft.studyplan.StudyPlan;
import seedu.classcraft.studyplan.ModuleStatus;
import seedu.classcraft.ui.Ui;
import seedu.classcraft.storage.Storage;

/**
 * @@author  lingru
 * A command to add a module as COMPLETED or EXEMPTED.
 */
public class AddExemptedCommand extends Command {

    private String moduleCode;
    private ModuleStatus status;

    /**
     * @@author lingru
     * Constructor for AddExemptedCommand.
     * @param moduleCode The module code to add.
     * @param status The status (must be COMPLETED or EXEMPTED).
     */
    public AddExemptedCommand(String moduleCode, ModuleStatus status) {
        super();
        this.moduleCode = moduleCode;
        this.status = status;
    }

    /**
     * @@author lingru
     * Executes the command to add a completed/exempted module to the study plan.
     * It prints a success message using the Ui, or prints the error message
     * if the module cannot be added (e.g., it already exists).
     *
     * @param studyPlan The study plan to modify.
     * @param ui The UI to display messages to the user.
     * @param storage The storage handler
     */
    @Override
    public void executeCommand(StudyPlan studyPlan, Ui ui, Storage storage) {
        try {
            studyPlan.addCompletedModule(moduleCode, status, storage, false);
            ui.showMessage("Successfully added " + moduleCode + " as " + status.toString());
        } catch (Exception e) {
            ui.showMessage(e.getMessage());
        }
    }
}