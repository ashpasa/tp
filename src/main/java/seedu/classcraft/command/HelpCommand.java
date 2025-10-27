package seedu.classcraft.command;

import seedu.classcraft.studyplan.StudyPlan;
import seedu.classcraft.ui.Ui;

public class HelpCommand extends Command {
    public HelpCommand() {
        super();
    }

    @Override
    public void executeCommand(StudyPlan studyPlan, Ui ui) {
        String userHelp = "Hi there, do you require help?\n"
                + "Here are the list of commands you can use:\n"
                + "1. add - Adds a Module.\n   Format: add n/{MODULE_CODE} s/{SEMESTER} (SEMESTER: 1 to 8)\n\n"
                + "2. delete - Deletes a Module.\n   Format: delete {MODULE_CODE}\n\n"
                + "3. view - View Current Plan, Sample Plan or Graduation Requirements.\n   Format: view {INFORMATION} "
                + "(INFORMATION: plan,grad,sample)\n\n"
                + "4. spec - View Modules required for a specialisation.\n   Format: spec {SPECIALISATION} "
                + "(SPECIALISATION: ae,4.0,iot,robotics,st)\n\n"
                + "5. prereq - View Pre-Requisites for the given module.\n   Format: prereq {MODULE_CODE}\n\n"
                + "6. exit - Exit the program\n\n"
                + "7. help - View this message again";
        System.out.println(userHelp);
    }
}
