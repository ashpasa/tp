package seedu.classcraft.command;

import seedu.classcraft.studyplan.StudyPlan;
import seedu.classcraft.ui.Ui;
import seedu.classcraft.storage.Storage;

/**
 * @@author lingru
 * A command to view the current degree progress percentage.
 */
public class ViewProgressCommand extends Command {

    public ViewProgressCommand() {
        super();
    }

    /**
     * @@author lingru
     * Executes the command to calculate and display degree progress.
     * Uses the Ui.showMessage method to format the output.
     *
     * @param studyPlan The study plan to get data from.
     * @param ui The UI to display the result.
     * @param storage The storage handler
     */
    @Override
    public void executeCommand(StudyPlan studyPlan, Ui ui, Storage storage) throws Exception {
        double percentage = studyPlan.getDegreeProgressPercentage();
        int securedMCs = studyPlan.getTotalSecuredMCs();
        int totalMCs = studyPlan.getTotalMcsForGraduation();

        String message = "Your Degree Progress: " + percentage + "%" + System.lineSeparator()
                + "Secured MCs: " + securedMCs + " / " + totalMCs;

        ui.showMessage(message);
    }
}

