package seedu.classcraft.command;

import seedu.classcraft.storage.Storage;
import seedu.classcraft.studyplan.StudyPlan;
import seedu.classcraft.ui.Ui;

// @@author ashpasa
public class CalcCreditsCommand extends Command {
    private int calculatedSemIndex;

    public CalcCreditsCommand(int calculatedSemIndex) {
        super();
        this.calculatedSemIndex = calculatedSemIndex;
    }

    @Override
    public void executeCommand(StudyPlan studyPlan, Ui ui, Storage storage) {
        int totalCredits = studyPlan.calculateSemCredits(calculatedSemIndex);
        ui.displayTotalCredits(calculatedSemIndex, totalCredits);
    }
}
// @@author
