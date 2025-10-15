package seedu.duke.command;

import seedu.duke.studyplan.StudyPlan;
import seedu.duke.ui.Ui;
import seedu.duke.command.Command;


public class InvalidCommand extends Command {
    public InvalidCommand() {
        super();
    }

    @Override
    public void executeCommand(StudyPlan studyPlan, Ui ui) {
        System.out.println("I'm sorry, but I don't know what that means :-(");
    }
}
