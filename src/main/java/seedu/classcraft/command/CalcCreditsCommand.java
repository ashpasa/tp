package seedu.classcraft.command;

import seedu.classcraft.storage.Storage;
import seedu.classcraft.studyplan.StudyPlan;
import seedu.classcraft.ui.Ui;

// @@author ashpasa
public class CalcCreditsCommand extends Command {
    private int calculatedSemIndex;

    /**
     * CalcCreditsCommand constructor to create a CalcCreditsCommand object.
     * 
     * @param calculatedSemIndex The semester index for which to calculate total credits.
     */
    public CalcCreditsCommand(int calculatedSemIndex) {
        super();
        this.calculatedSemIndex = calculatedSemIndex;
    }

    /**
     * Method from Command parent class. Uses the studyPlan's calculateSemCredits method to
     * calculate total credits for the specified semester index.
     * 
     * @param studyPlan The current study plan, including data restored from storage.
     * @param ui        The user interface to interact with the user.
     * @param storage   The storage handler to read/write data.
     */
    @Override
    public void executeCommand(StudyPlan studyPlan, Ui ui, Storage storage) {
        int totalCredits = studyPlan.calculateSemCredits(calculatedSemIndex);
        ui.displayTotalCredits(calculatedSemIndex, totalCredits);
    }
}
// @@author
