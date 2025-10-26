package seedu.classcraft.command;

public class CalcCreditsCommand extends Command {
    private int calculatedSemIndex;

    public CalcCreditsCommand(int calculatedSemIndex) {
        super();
        this.calculatedSemIndex = calculatedSemIndex;
    }

    @Override
    public void executeCommand(seedu.classcraft.studyplan.StudyPlan studyPlan, seedu.classcraft.ui.Ui ui) {
        int totalCredits = studyPlan.calculateSemCredits(calculatedSemIndex);
        ui.displayTotalCredits(calculatedSemIndex, totalCredits);
    }
}
