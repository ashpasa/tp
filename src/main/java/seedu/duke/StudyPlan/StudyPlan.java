package seedu.duke.StudyPlan;

import seedu.duke.exceptions.StudyPlanException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Creates a study plan based on modules added by user
 */
public class StudyPlan {
    ArrayList<ArrayList<String>> studyPlan = new ArrayList<>();
    HashMap<String, Integer> modules; // stores moduleCode: semester

    public StudyPlan(int totalSemesters) {
        try {
            if (!((totalSemesters > 0) && (totalSemesters <= 8))) {
                throw new StudyPlanException("An invalid number of semesters was input when creating StudyPlan");
            }
            for (int i = 0; i < totalSemesters; i++) {
                ArrayList<String> innerList = new ArrayList<>();
                studyPlan.add(innerList);
            }
            modules = new HashMap<>();
        } catch (StudyPlanException e) {
            System.out.println("Please input a valid number of semesters, from 1 to 8");
        }
    }

    // queries for module info, prints prereqs and adds to study plan based on semester
    public void addModule(String moduleString, int semester) throws Exception {
        ModuleHandler fetcher = new ModuleHandler();
        String prereqs = fetcher.getModulePrerequisites(moduleString);
        System.out.println("Prerequisites for " + moduleString + ": " + prereqs);

        studyPlan.get(semester - 1).add(moduleString);
        modules.put(moduleString, semester);

        System.out.println("Added " + moduleString + " to semester " + prereqs);
    }

    public void removeModule(String moduleString) {
        Integer sem = modules.get(moduleString);
        studyPlan.get(sem - 1).remove(moduleString);
        modules.remove(moduleString);
        System.out.println("Removed " + moduleString);
    }

    public ArrayList<ArrayList<String>> getStudyPlan() {
        return studyPlan;
    }
}
