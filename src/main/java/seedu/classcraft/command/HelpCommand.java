package seedu.classcraft.command;

import seedu.classcraft.storage.Storage;
import seedu.classcraft.studyplan.StudyPlan;
import seedu.classcraft.ui.Ui;

/**
 * HelpCommand class representing the command to display help information.
 */
public class HelpCommand extends Command {

    /**
     * Method from Command parent class.
     * Prints a help message to the user, indicating available commands
     * and their required formats.
     *
     * @param studyPlan The current study plan ,including data restored from storage
     * @param ui The user interface to interact with the user
     * @param storage The storage handler to read/write data
     */
    @Override
    public void executeCommand(StudyPlan studyPlan, Ui ui, Storage storage) {
        String userHelp = "Hi there, do you require help?\n"
                + "Here are the list of commands you can use:\n"
                + "1. add - Adds a Module.\n   Format: add n/{MODULE_CODE} s/{SEMESTER} (SEMESTER: 1 to 8)\n\n"
                + "2. delete - Deletes a Module.\n   Format: delete {MODULE_CODE}\n\n"
                + "3. view - View Current Plan, Sample Plan or Graduation Requirements.\n   Format: view {INFORMATION} "
                + "(INFORMATION: plan,grad,sample)\n\n"
                + "4. spec - View Modules required for a specialisation.\n   Format: spec {SPECIALISATION} "
                + "(SPECIALISATION: ae,4.0,iot,robotics,st)\n\n"
                + "5. prereq - View Pre-Requisites for the given module.\n   Format: prereq {MODULE_CODE}\n\n"
                + "6. balance - Balances workload of study plan. \n   Format: balance\n\n"
                + "7. exit - Exit the program\n\n"
                + "8. help - View this message again";
        ui.printMessage(userHelp);
    }
}
