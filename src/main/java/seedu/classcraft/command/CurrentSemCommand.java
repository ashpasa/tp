package seedu.classcraft.command;


import seedu.classcraft.storage.Storage;
import seedu.classcraft.studyplan.StudyPlan;
import seedu.classcraft.ui.Ui;

/**
 * CurrentSemCommand class representing the command to set the value of the current semester.
 * Extends the Command abstract class and implements the executeCommand method.
 */
public class CurrentSemCommand extends Command {
    private String currentSem;

    /**
     * CurrentSemCommand constructor to create an CurrentSemCommand object.
     *
     * @param currentSem a String object that indicates the current sem the user is in
     */
    public CurrentSemCommand(String currentSem) {
        super();
        this.currentSem = currentSem;
    }

    /**
     * Method from Command parent class.
     * Checks if the input is a valid semester before setting the currentSemester attribute in
     * the StudyPlan object.
     *
     * @param studyPlan The current study plan ,including data restored from storage
     * @param ui        The user interface to interact with the user
     * @param storage   The storage handler to read/write data
     */
    @Override
    public void executeCommand(StudyPlan studyPlan, Ui ui, Storage storage) {
        try {
            int totalSemesters = StudyPlan.getTotalSemesters();
            int currentSemester = Integer.parseInt(currentSem);

            if (currentSemester < 1 || currentSemester > totalSemesters) {
                throw new IllegalArgumentException();
            }

            StudyPlan.setCurrentSemester(Integer.parseInt(currentSem));

            ui.showMessage("Successfully set current semester to " + currentSem);
        } catch (IllegalArgumentException e) {
            // Handle invalid semester errors
            ui.printMessage("Invalid semester");
        } catch (Exception e) {
            // Handle other errors (API failures, etc.)
            ui.printMessage("Error adding module: " + e.getMessage());
        }
    }
}
