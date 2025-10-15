package seedu.duke.command;

import seedu.duke.studyplan.StudyPlan;
import seedu.duke.ui.Ui;

public class ExitCommand extends Command {
    public ExitCommand() {
        super();
    }

    @Override
    public void executeCommand(StudyPlan studyPlan, Ui ui) {
        System.out.println("Exiting the program. Goodbye!");
        System.exit(0);
    }
}
