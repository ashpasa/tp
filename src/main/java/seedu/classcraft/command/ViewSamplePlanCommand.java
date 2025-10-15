package seedu.classcraft.command;

import seedu.classcraft.studyplan.StudyPlan;
import seedu.classcraft.ui.Ui;

/**
 * Command to generate and display the default sample study plan for CEG.
 * User input: view sample
 */
public class ViewSamplePlanCommand extends Command {

    @Override
    public void executeCommand(StudyPlan currentStudyPlan, Ui ui) throws Exception {
        StudyPlan samplePlan = StudyPlan.getSampleStudyPlan();

        ui.displaySamplePlan(samplePlan);
    }
}

