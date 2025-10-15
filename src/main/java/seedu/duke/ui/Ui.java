package seedu.duke.ui;

import seedu.duke.studyplan.StudyPlan;
import seedu.duke.studyplan.Module;
import java.util.ArrayList;

public class Ui {
    private String line = "_____________________________________________________" + System.lineSeparator();

    public void printMessage(String message) {
        System.out.print(line);
        System.out.println(message);
        System.out.print(line);
    }

    /**
     * Prints the contents of a study plan, showing modules by semester.
     * @param plan The study plan data (either current or sample).
     * @param title The title to display (e.g., "CEG Sample Study Plan").
     */
    private void displayStudyPlan(StudyPlan plan, String title) {
        System.out.print(line);
        System.out.println(title);
        System.out.print(line);

        ArrayList<ArrayList<Module>> planData = plan.getStudyPlan();

        for (int i = 0; i < planData.size(); i++) {
            System.out.println("Semester " + (i + 1) + ":");
            ArrayList<Module> semesterMods = planData.get(i);

            if (semesterMods.isEmpty()) {
                System.out.println("  (Empty)");
                continue;
            }

            for (Module mod : semesterMods) {
                String prereqsInfo = mod.getPrerequisitesDisplay();

                System.out.println("  - " + mod.getModCode() + " (" + mod.getModName() + ")" + prereqsInfo);
            }
        }
        System.out.print(line);
    }

    public void displaySamplePlan(StudyPlan samplePlan) {
        displayStudyPlan(samplePlan, "CEG Sample Study Plan");
    }

    public void displayCurrentPlan(StudyPlan currentPlan) {
        displayStudyPlan(currentPlan, "Current Study Plan");
    }
}
